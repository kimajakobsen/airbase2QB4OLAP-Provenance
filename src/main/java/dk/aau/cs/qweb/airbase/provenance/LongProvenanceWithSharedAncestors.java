package dk.aau.cs.qweb.airbase.provenance;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import dk.aau.cs.qweb.airbase.types.Object;
import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.types.Tuple;
import dk.aau.cs.qweb.airbase.vocabulary.XSD;

public class LongProvenanceWithSharedAncestors implements ProvenanceFlow {
	
	Entity provenanceIdentifierEntity;
	ProvenanceSignature signature;

	public LongProvenanceWithSharedAncestors(ProvenanceSignature signature) {
		this.signature = signature;
		Entity rawFile = rawFile();
		
		Activity aggregation = aggregation(rawFile);
		
		List<Agent> countryOrganizations = extractAgentsFromXml();
		Agent owner = getEuropeanEnvironmentAgency();
		
		Entity file = file(aggregation, countryOrganizations, owner);
		
		Agent thisSoftware = thisSoftware();
				
		Activity extract = extract(file, thisSoftware);
		
		Entity tuple = tuple(extract);

		Agent kim = getKim();
		
		Entity mapping = mapping(kim);
		
		Activity createSubject = createSubject(tuple, mapping);
		
		Entity raw = raw(createSubject);
		
		provenanceIdentifierEntity = raw;
	}

//	private Entity quad(Activity setGraphLabel) {
//		Entity quad = new ProvenanceIdentifierEntity();
//		quad.wasGeneratedBy(setGraphLabel);
//		quad.setCustomProperty(Config.getNamespace()+"copyrightURL", new Object("http://www.eea.europa.eu/legal/copyright",XSD.stringType));
//		return quad;
//	}
//
//	private Activity setGraphLabel(Entity informationTriple) {
//		Activity setGraphLabel = new Activity("setGraphLabel");
//		setGraphLabel.used(informationTriple);
//		return setGraphLabel;
//	}
//
//	private Entity informationTriple(Activity clean) {
//		Entity informationTriple = new Entity("informationTriple");
//		informationTriple.wasGeneratedBy(clean);
//		return informationTriple;
//	}
//
//	private Activity clean(Agent thisSoftware, Entity raw) {
//		Activity clean = new Activity(getCallbackClassName(signature));
//		clean.used(raw);
//		clean.wasAssociatedWith(thisSoftware);
//		return clean;
//	}

	private Entity raw(Activity createSubject) {
		ProvenanceIdentifierEntity raw = new ProvenanceIdentifierEntity("provenanceIdentifier",getCallbackClassName(signature)+"/"+signature.getTuple().getLineCount());
		raw.wasGeneratedBy(createSubject);
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		raw.generatedAtTime(new Object(timeStamp,XSD.dateType));
		return raw;
	}

	private Activity createSubject(Entity tuple, Entity mapping) {
		Activity createSubject = new Activity("createSubject");
		createSubject.used(mapping);
		createSubject.used(tuple);
		return createSubject;
	}

	private Entity mapping(Agent kim) {
		Entity mapping = new Entity("mapping");
		mapping.wasAttributedTo(kim);
		mapping.generatedAtTime(new Object("2016-11-18",XSD.dateType));
		return mapping;
	}

	private Entity tuple(Activity extract) {
		Entity tuple = new Entity("tuple",signature);
		tuple.wasGeneratedBy(extract);
		if (isQualityApproved(signature)) {
			tuple.setCustomProperty(Config.getNamespace()+"qualityApproved", new Object("True",XSD.booleanType)); 
		}
		return tuple;
	}

	private Activity extract(Entity file, Agent thisSoftware) {
		Activity extract = new Activity("extract");
		extract.used(file);
		extract.wasAssociatedWith(thisSoftware);
		return extract;
	}

	private Entity file(Activity activity, List<Agent> countryOrganizations, Agent owner) {
		Entity file = new Entity("file",signature.getFileName());
		file.atLocation(new Object(signature.getFilePath(),XSD.stringType));
		file.wasAttributedTo(countryOrganizations);
		file.wasAttributedTo(owner);
		file.wasGeneratedBy(activity);
		file.setCustomProperty(Config.getNamespace()+"copyrightURL", new Object("http://www.eea.europa.eu/legal/copyright",XSD.stringType));
		return file;
	}

	private Activity aggregation(Entity rawFile) {
		Activity aggregation = new Activity("aggregation",signature.getRawDataFileName());
		aggregation.used(rawFile);
		return aggregation;
	}

	private Entity rawFile() {
		Entity rawFile = new Entity("rawFile",signature.getRawDataFileName());
		rawFile.atLocation(new Object(signature.getRawDataFilePath(),XSD.stringType));
		return rawFile;
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
		CallBack callback = null;
		if (signature.getLevel().equals("http://qweb.cs.aau.dk/airbase/schema/value")) {
			//If measure
			callback = Airbase2QB4OLAP.getCallBackFunctionPredicate("measure");
		} else {
			callback = Airbase2QB4OLAP.getCallBackFunctionPredicate(signature.getQuad().getPredicate());
		}
		String[] dotSplit = callback.getClass().getName().split("\\.");
		return dotSplit[dotSplit.length-1];
	}

	private Agent getKim() {
		Agent agent = new Person("kim");
		agent.setCustomProperty(FOAF.name.toString(),new Object("Kim Ahlstrøm",XSD.stringType));
		agent.setCustomProperty(FOAF.mbox.toString(),"kah@cs.aau.dk");
		return agent;
	}

	private Agent thisSoftware() {
		Agent agent = new Software("airbase2QB4OLAP-Provenance");
		agent.setCustomProperty(FOAF.name.toString(),"airbase2QB4OLAP-Provenance");
		return agent;
	}

	private Agent getEuropeanEnvironmentAgency() {
		Agent agent = new Organization("European_Environment_Agency");
		agent.setCustomProperty(FOAF.name.toString(),"airbase2QB4OLAP-Provenance");
		agent.setCustomProperty(FOAF.homepage.toString(),"http://www.eea.europa.eu");
		agent.atLocation(new Object("Kongens Nytorv 6, 1050, Copenhagen, Denmark",XSD.stringType));
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

				if (nNode.getNodeType() == Node.ELEMENT_NODE ) {
					Element eElement = (Element) nNode;
					
					NodeList firstname = eElement.getElementsByTagName("person_first_name");
					NodeList lastname = eElement.getElementsByTagName("person_last_name");
					NodeList email = eElement.getElementsByTagName("person_email_address");
					NodeList title = eElement.getElementsByTagName("person_title");
					
					if (firstname.getLength() > 0 && lastname.getLength() > 0) {
						Agent agent = new Person(stringify(firstname.item(0).getTextContent()+"_"+lastname.item(0).getTextContent()));;
						
						agent.setCustomProperty(Config.getNamespace()+"title", eElement.getParentNode().getNodeName());
						agent.setCustomProperty(FOAF.firstName.toString(), firstname.item(0).getTextContent());
						agent.setCustomProperty(FOAF.family_name.toString(), lastname.item(0).getTextContent());
						
						if (title.getLength() > 0) {
							agent.setCustomProperty(FOAF.title.toString(), title.item(0).getTextContent());
						}
						if (email.getLength() > 0 ) {
							agent.setCustomProperty(FOAF.mbox.toString(), email.item(0).getTextContent());
						}
						agents.add(agent);
					}
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
