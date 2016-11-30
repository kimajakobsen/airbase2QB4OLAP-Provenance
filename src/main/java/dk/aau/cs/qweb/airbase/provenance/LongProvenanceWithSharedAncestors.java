package dk.aau.cs.qweb.airbase.provenance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.jena.sparql.vocabulary.FOAF;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dk.aau.cs.qweb.airbase.Airbase2QB4OLAP;
import dk.aau.cs.qweb.airbase.Config;
import dk.aau.cs.qweb.airbase.callback.CallBack;
import dk.aau.cs.qweb.airbase.provenance.provo.Activity;
import dk.aau.cs.qweb.airbase.provenance.provo.Agent;
import dk.aau.cs.qweb.airbase.provenance.provo.Entity;
import dk.aau.cs.qweb.airbase.provenance.provo.Organization;
import dk.aau.cs.qweb.airbase.provenance.provo.Person;
import dk.aau.cs.qweb.airbase.provenance.provo.ProvenanceIdentifierEntity;
import dk.aau.cs.qweb.airbase.provenance.provo.Software;
import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.types.Tuple;
import dk.aau.cs.qweb.airbase.vocabulary.XSD;

public class LongProvenanceWithSharedAncestors implements ProvenanceFlow {
	
	Entity provenanceIdentifierEntity;

	public LongProvenanceWithSharedAncestors(ProvenanceSignature signature) {
		Entity rawFile = new Entity("rawFile");
		rawFile.atLocation("");
		
		Activity aggregation = new Activity("aggregation");
		aggregation.used(rawFile);
		
		Entity aggregatedData = new Entity("aggregatedData");
		aggregatedData.atLocation("");
		aggregatedData.wasGeneratedBy(aggregation);
		
		Activity download = new Activity("download");
		download.used(aggregatedData);
		
		List<Agent> countryOrganizations = extractAgentsFromXml();
		Agent owner = getEuropeanEnvironmentAgency();
		
		Entity file = new Entity("file");
		file.atLocation(signature.getFilePath());
		file.wasAttributedTo(countryOrganizations);
		file.wasAttributedTo(owner);
		file.setCustomProperty(Config.getNamespace()+"copyrightURL", "http://www.eea.europa.eu/legal/copyright");
		if (isQualityApproved(signature)) {
			file.setCustomProperty(Config.getNamespace()+"qualityApproved", "True"+XSD.booleanType); 
		}
		
		Agent thisSoftware = thisSoftware();
				
		Activity extract = new Activity("extract");
		extract.used(file);
		extract.wasAssociatedWith(thisSoftware);
		
		Entity tuple = new Entity("tuple");
		tuple.wasGeneratedBy(extract);

		Agent kim = getKim();
		
		Entity mapping = new Entity("mapping");
		mapping.wasAttributedTo(kim);
		mapping.generatedAtTime("November182016");
		
		Activity createSubject = new Activity("craeteSubject");
		createSubject.used(mapping);
		createSubject.used(tuple);
		
		Entity raw = new Entity("raw");
		raw.wasGeneratedBy(createSubject);

		Activity clean = new Activity(getCallbackClassName(signature));
		clean.used(raw);
		clean.wasAssociatedWith(thisSoftware);
		
		Entity informationTriple = new Entity("informationTriple");
		informationTriple.wasGeneratedBy(clean);
		
		Activity setGraphLabel = new Activity("setGraphLabel");
		setGraphLabel.used(informationTriple);
		
		Entity quad = new ProvenanceIdentifierEntity();
		quad.wasGeneratedBy(setGraphLabel);
		quad.setCustomProperty(Config.getNamespace()+"copyrightURL", "http://www.eea.europa.eu/legal/copyright");
		provenanceIdentifierEntity = quad;
		
	}

	private boolean isQualityApproved(ProvenanceSignature signature) {
		String[] split = signature.getFilePath().split("/");
		String fileName = split[split.length-1];
		String[] words = fileName.split("_");
		
		
		if (words[words.length-1].equals("statistics.csv")) {
			Tuple tuple = signature.getTuple();
			int year = Integer.parseInt(tuple.getValue("statistics_year"));
			if (year >= 2002) {
				return true;
			}	
		}
		return false;
	}

	private String getCallbackClassName(ProvenanceSignature signature) {
		CallBack callback = Airbase2QB4OLAP.getCallBackFunctionPredicate(signature.getQuad().getPredicate());
		return callback.getClass().getName();
	}

	private Agent getKim() {
		Agent agent = new Person("kim");
		agent.setCustomProperty(FOAF.name.toString(),"Kim Ahlstrøm");
		agent.setCustomProperty(FOAF.mbox.toString(),"kah@cs.aau.dk");
		return agent;
	}

	private Agent thisSoftware() {
		Agent agent = new Software("airbase2QB4OLAP-Provenance");
		agent.setCustomProperty(FOAF.name.toString(),"airbase2QB4OLAP-Provenance");
		return agent;
	}

	private Agent getEuropeanEnvironmentAgency() {
		Agent agent = new Organization("European Environment Agency");
		agent.setCustomProperty(FOAF.name.toString(),"airbase2QB4OLAP-Provenance");
		agent.setCustomProperty(FOAF.homepage.toString(),"http://www.eea.europa.eu");
		agent.atLocation("Kongens Nytorv 6, 1050, Copenhagen, Denmark");
		return agent;
	}

	private List<Agent> extractAgentsFromXml() {
		List<Agent> agents = new ArrayList<Agent>();
		try {
			File xmlFile = new File(Config.getXMLfilePath());
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			NodeList organization = doc.getElementsByTagName("organization");
			for (int temp = 0; temp < organization.getLength(); temp++) {
				Node nNode = organization.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					Agent agent = new Organization(stringify(eElement.getElementsByTagName("organization_name").item(0).getTextContent()));;
					
					agent.setCustomProperty(Config.getNamespace()+"title", eElement.getParentNode().getNodeName());
					agent.setCustomProperty(FOAF.name.toString(), eElement.getElementsByTagName("organization_name").item(0).getTextContent());
					agent.setCustomProperty(Config.getNamespace()+"address", eElement.getElementsByTagName("organization_address").item(0).getTextContent());
					agent.setCustomProperty(Config.getNamespace()+"city", eElement.getElementsByTagName("organization_city").item(0).getTextContent());
					agent.setCustomProperty(FOAF.phone.toString(), eElement.getElementsByTagName("organization_phone_number").item(0).getTextContent());
					agent.setCustomProperty(Config.getNamespace()+"fax", eElement.getElementsByTagName("organization_fax_number").item(0).getTextContent());
					
					agents.add(agent);
				}
			}
			
			NodeList person = doc.getElementsByTagName("person");
			for (int temp = 0; temp < person.getLength(); temp++) {
				Node nNode = person.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					Agent agent = new Person(stringify(eElement.getElementsByTagName("person_first_name").item(0).getTextContent()+eElement.getElementsByTagName("person_last_name").item(0).getTextContent()));;
					
					agent.setCustomProperty(Config.getNamespace()+"title", eElement.getParentNode().getNodeName());
					agent.setCustomProperty(FOAF.firstName.toString(), eElement.getElementsByTagName("person_first_name").item(0).getTextContent());
					agent.setCustomProperty(FOAF.family_name.toString(), eElement.getElementsByTagName("person_last_name").item(0).getTextContent());
					agent.setCustomProperty(FOAF.mbox.toString(), eElement.getElementsByTagName("person_email_address").item(0).getTextContent());
					
					agents.add(agent);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		 
		return agents;
	}
	
	private String stringify(String textContent) {
		return textContent.replaceAll(" ", "_");
	}

	@Override
	public Set<Quad> getQuads() {
		return provenanceIdentifierEntity.getQuads();
	}

	@Override
	public String getProvenanceIdentifier() {
		return provenanceIdentifierEntity.getSubject();
	}
}
