import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;
import java.util.*;

public class UrbanInfrastructureDevelopment implements Serializable {
    static final long serialVersionUID = 88L;

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        // TODO: YOUR CODE HERE
        for (Project project : projectList) {
            project.printSchedule(project.getEarliestSchedule());
        }
    }

    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = new ArrayList<>();
        // TODO: YOUR CODE HERE
        try {
            File file = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList projectNodes = doc.getElementsByTagName("Project");
            for (int i = 0; i < projectNodes.getLength(); i++) {
                Node projectNode = projectNodes.item(i);
                if (projectNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element projectElement = (Element) projectNode;
                    String projectName = projectElement.getElementsByTagName("Name").item(0).getTextContent();
                    List<Task> tasks = new ArrayList<>();
                    NodeList taskNodes = projectElement.getElementsByTagName("Task");
                    for (int j = 0; j < taskNodes.getLength(); j++) {
                        Node taskNode = taskNodes.item(j);
                        if (taskNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element taskElement = (Element) taskNode;
                            int taskId = Integer.parseInt(taskElement.getElementsByTagName("TaskID").item(0).getTextContent());
                            String taskDescription = taskElement.getElementsByTagName("Description").item(0).getTextContent();
                            int taskDuration = Integer.parseInt(taskElement.getElementsByTagName("Duration").item(0).getTextContent());
                            List<Integer> dependencies = new ArrayList<>();
                            NodeList dependencyNodes = taskElement.getElementsByTagName("DependsOnTaskID");
                            for (int k = 0; k < dependencyNodes.getLength(); k++) {
                                Node dependencyNode = dependencyNodes.item(k);
                                if (dependencyNode.getNodeType() == Node.ELEMENT_NODE) {
                                    dependencies.add(Integer.parseInt(dependencyNode.getTextContent()));
                                }
                            }
                            tasks.add(new Task(taskId, taskDescription, taskDuration, dependencies));
                        }
                    }
                    projectList.add(new Project(projectName, tasks));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectList;
    }
}
