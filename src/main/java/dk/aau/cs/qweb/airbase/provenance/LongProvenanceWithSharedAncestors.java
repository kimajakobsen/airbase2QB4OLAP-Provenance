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
	private static Entity ontology; 
	Entity provenanceIdentifierEntity;
	List<Entity> provenanceIdentifierSubentities;
	ProvenanceSignature signature;

	public LongProvenanceWithSharedAncestors(ProvenanceSignature signature) {
		this.signature = signature;
		Entity rawFile = rawFile();
		
		Activity aggregation = aggregation(rawFile);
		
		List<Agent> countryOrganizations = extractAgentsFromXml();
		Agent owner = getEuropeanEnvironmentAgency();
		Agent luis = getLuis();	
		Agent kim = getKim();
		provenanceIdentifierSubentities = new ArrayList<>();

		Entity[] composingFiles = new Entity[signature.getFiles().size()];
		
		for (int i = 0; i < signature.getFiles().size(); ++i) {
			String suffix = signature.getFiles().get(i);
			composingFiles[i] = file(aggregation, signature.getFileName(suffix), signature.getRemoteFileName(suffix), countryOrganizations, owner);	
			Activity createSubjectForInputFile = createTriple(composingFiles[i], luis);
			provenanceIdentifierSubentities.add(inputFileEntity(createSubjectForInputFile));
		}
		
		Activity join = join(luis, signature.getFileName(), composingFiles);
		
		Entity finalFile = file(join, "airbase.csv", luis, composingFiles);
		
		Agent thisSoftware = thisSoftware();
				
		Activity extract = extract(finalFile, thisSoftware);
		
		Entity tuple = tuple(extract);
		
		Entity theOntology = createOntology(kim);
		
		Entity mapping = mapping(kim, theOntology);
		
		Activity createSubject = createTriple(tuple, mapping);
		
		Entity raw = null;
		raw = raw(createSubject);
		provenanceIdentifierEntity = raw;
	}


	private Entity raw(Activity createSubject) {
		ProvenanceIdentifierEntity raw = null;
		if (signature.isObservation()) {
			raw = new ProvenanceIdentifierEntity("provenanceIdentifier",getCallbackClassName(signature)+"/"+ signature.getTuple().getValue("country_iso_code") +signature.getTuple().getLineCount());
		} else {
			raw = new ProvenanceIdentifierEntity("provenanceIdentifier", getCallbackClassName(signature) + "/" + Airbase2QB4OLAP.removePrefix(signature.getLevel()) + "/" + Airbase2QB4OLAP.getSuffixUsedInIRI(signature.getLevel(), signature.getTuple()));
		}
		raw.wasGeneratedBy(createSubject);
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		raw.generatedAtTime(new Object(timeStamp,XSD.dateType));
		return raw;
	}


	private Entity createOntology(Agent agent) {
		if (ontology == null) {
			ontology = new Entity("ontology", "Airbase.ttl");
			ontology.generatedAtTime(new Object("2017-04-10", XSD.dateType));
			ontology.wasAttributedTo(agent);
		}
		
		return ontology;
	}


	private Entity inputFileEntity(Activity createSubjectForFile) {
		ProvenanceIdentifierEntity raw = new ProvenanceIdentifierEntity("provenanceIdentifier", "file/"+ createSubjectForFile.getShortName());
		raw.wasGeneratedBy(createSubjectForFile);
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		raw.generatedAtTime(new Object(timeStamp,XSD.dateType));
		return raw;
	}


	private Activity createTriple(Entity entity, Agent author) {
		Activity activity = new Activity("createTriple", entity.getShortName());
		activity.wasAssociatedWith(author);
		return activity;
	}


	private Activity join(Agent author, String joinedName, Entity ...composingFiles) {
		Activity activity = new Activity("join", joinedName);
		activity.wasAssociatedWith(author);
		return activity;
	}

	private Activity createTriple(Entity tuple, Entity mapping) {
		Activity createSubject = new Activity("createTriple");
		createSubject.used(mapping);
		createSubject.used(tuple);
		return createSubject;
	}

	private Entity mapping(Agent kim, Entity theOntology) {
		Entity mapping = new Entity("mapping");
		mapping.wasAttributedTo(kim);
		mapping.generatedAtTime(new Object("2016-11-18",XSD.dateType));
		mapping.wasDerivedFrom(theOntology);
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

	private Entity file(Activity activity, String fileName, String fileRemoteLocation, List<Agent> countryOrganizations, Agent owner) {
		Entity file = new Entity("file", fileName);
		file.atLocation(new Object(fileRemoteLocation, XSD.stringType));
		file.wasAttributedTo(countryOrganizations);
		file.wasAttributedTo(owner);
		file.wasGeneratedBy(activity);
		file.setCustomProperty(Config.getNamespace()+"copyrightURL", new Object("http://www.eea.europa.eu/legal/copyright",XSD.stringType));
		return file;
	}
	
	private Entity file(Activity activity, String fileName, Agent author, Entity ...components) {
		Entity file = new Entity(activity, "file", fileName, components);
		file.wasGeneratedBy(activity);
		file.wasAttributedTo(author);
		return file;
	}

	private Activity aggregation(Entity rawFile) {
		Activity aggregation = new Activity("aggregation",signature.getRawDataFileName());
		aggregation.used(rawFile);
		return aggregation;
	}

	private Entity rawFile() {
		Entity rawFile = new Entity("rawFile",signature.getRawDataFileName());
		rawFile.atLocation(new Object(signature.getRawDataFileName(),XSD.stringType));
		return rawFile;
	}

	private boolean isQualityApproved(ProvenanceSignature signature) {
		Tuple tuple = signature.getTuple();
		int year = Integer.parseInt(tuple.getValue("statistics_year"));
		if (year >= 2002) {
			return true;
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
	
	private Agent getLuis() {
		Agent agent = new Person("luis");
		agent.setCustomProperty(FOAF.name.toString(),new Object("Luis Galárraga",XSD.stringType));
		agent.setCustomProperty(FOAF.mbox.toString(),"galarraga@cs.aau.dk");
		return agent;
	}

	private Agent thisSoftware() {
		Agent agent = new Software("airbase2QB4OLAP-Provenance");
		agent.setCustomProperty(FOAF.name.toString(),"airbase2QB4OLAP-Provenance");
		return agent;
	}

	private Agent getEuropeanEnvironmentAgency() {
		Agent agent = new Organization("European_Environment_Agency");
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
					
					if (eElement.getParentNode() != null)
						agent.setCustomProperty(Config.getNamespace()+"title", eElement.getParentNode().getNodeName());
					
					if (eElement.getElementsByTagName("organization_name").item(0) != null)
						agent.setCustomProperty(FOAF.name.toString(), eElement.getElementsByTagName("organization_name").item(0).getTextContent());
					
					if (eElement.getElementsByTagName("organization_address").item(0) != null)
						agent.setCustomProperty(Config.getNamespace()+"address", eElement.getElementsByTagName("organization_address").item(0).getTextContent());
					
					if (eElement.getElementsByTagName("organization_city").item(0) != null)
						agent.setCustomProperty(Config.getNamespace()+"city", eElement.getElementsByTagName("organization_city").item(0).getTextContent());
					
					if (eElement.getElementsByTagName("organization_phone_number").item(0) != null)
						agent.setCustomProperty(FOAF.phone.toString(), eElement.getElementsByTagName("organization_phone_number").item(0).getTextContent());
					
					if (eElement.getElementsByTagName("organization_fax_number").item(0) != null)
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
						Agent agent = new Person(stringify(firstname.item(0).getTextContent()+"_"+lastname.item(0).getTextContent()));
						
						if (eElement.getParentNode() != null)
							agent.setCustomProperty(Config.getNamespace()+"title", eElement.getParentNode().getNodeName());
						if (firstname.item(0) != null)
							agent.setCustomProperty(FOAF.firstName.toString(), firstname.item(0).getTextContent());
						if (lastname.item(0) != null)
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
