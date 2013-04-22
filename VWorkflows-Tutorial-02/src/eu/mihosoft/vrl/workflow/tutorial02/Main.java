/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.workflow.tutorial02;

import javafx.application.Application;
import javafx.stage.Stage;

import eu.mihosoft.vrl.workflow.FlowFactory;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VNode;
import eu.mihosoft.vrl.workflow.fx.FXSkinFactory;
import javafx.scene.Scene;
import jfxtras.labs.scene.layout.ScalableContentPane;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Main extends Application {

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // create scalable root pane
        ScalableContentPane canvas = new ScalableContentPane();

        // define background style
        canvas.setStyle("-fx-background-color: linear-gradient(to bottom, rgb(10,32,60), rgb(42,52,120));");

        // create a new flow object
        VFlow flow = FlowFactory.newFlow();

        // make it visible
        flow.setVisible(true);

        // create two nodes:
        // one leaf node and one subflow which is returned by createNodes
        createFlow(flow, 3, 10);

        // create skin factory for flow visualization
        FXSkinFactory fXSkinFactory = new FXSkinFactory(canvas.getContentPane());

        // generate the ui for the flow
        flow.setSkinFactory(fXSkinFactory);

        // show the main stage/window
        showStage(canvas, primaryStage);

    }

    private void showStage(ScalableContentPane canvas, Stage primaryStage) {
        // the usual application setup
        Scene scene = new Scene(canvas, 800, 800);
        primaryStage.setTitle("VWorkflows Tutorial 02");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates a flow with specified width and depth.
     * @param workflow parent workflow
     * @param depth flow depth (number of nested nodes)
     * @param width flow width (number of nodes per layer)
     */
    public void createFlow(VFlow workflow, int depth, int width) {

        // stop if we reached deppest layer
        if (depth < 1) {
            return;
        }

        // create nodes in current layer
        for (int i = 0; i < width; i++) {

            VNode n;

            // every second node shall be a subflow
            if (i % 2 == 0) {
                // create subflow
                VFlow subFlow = workflow.newSubFlow();
                n = subFlow.getModel();
                createFlow(subFlow, depth - 1, width);
            } else {
                //create leaf node
                n = workflow.newNode();
            }

            n.setTitle("Node " + i);

            // every third node shall have the same connection type
            // colors for "control", "data" and "event" are currently hardcoded
            // in skin. This will change!
            if (i % 3 == 0) {
                n.setInput(true, "control");
                n.setOutput(true, "control");
            } else if (i % 3 == 1) {
                n.setInput(true, "data");
                n.setOutput(true, "data");
            } else if (i % 3 == 2) {
                n.setInput(true, "event");
                n.setOutput(true, "event");
            }

            // specify node size
            n.setWidth(300);
            n.setHeight(200);

            // gap between nodes
            int gap = 30;
            
            int numNodesPerRow = 5;
            
            // specify node position (we use grid layout)
            n.setX((i % numNodesPerRow) * (n.getWidth() + gap));
            n.setY((i / numNodesPerRow) * (n.getHeight() + gap));
        }
    }

}
