/*
 * Copyright (c) 2024 Leyramu Group. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package leyramu.framework.lersosa.workflow.flowable;

import lombok.Data;
import org.flowable.bpmn.model.Event;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.image.ProcessDiagramGenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;
import java.util.*;

/**
 * 在 BPMN 2.0 流程中根据图表交换信息生成图像的类.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
public class CustomDefaultProcessDiagramGenerator implements ProcessDiagramGenerator {

    protected Map<Class<? extends BaseElement>, ActivityDrawInstruction> activityDrawInstructions = new HashMap<>();
    protected Map<Class<? extends BaseElement>, ArtifactDrawInstruction> artifactDrawInstructions = new HashMap<>();

    public CustomDefaultProcessDiagramGenerator() {
        this(1.0);
    }

    // The instructions on how to draw a certain construct is
    // created statically and stored in a map for performance.
    public CustomDefaultProcessDiagramGenerator(final double scaleFactor) {
        // start event
        activityDrawInstructions.put(StartEvent.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            StartEvent startEvent = (StartEvent) flowNode;
            if (startEvent.getEventDefinitions() != null && !startEvent.getEventDefinitions().isEmpty()) {
                EventDefinition eventDefinition = startEvent.getEventDefinitions().getFirst();
                switch (eventDefinition) {
                    case TimerEventDefinition _ -> processDiagramCanvas.drawTimerStartEvent(graphicInfo, scaleFactor);
                    case ErrorEventDefinition _ -> processDiagramCanvas.drawErrorStartEvent(graphicInfo, scaleFactor);
                    case EscalationEventDefinition _ ->
                        processDiagramCanvas.drawEscalationStartEvent(graphicInfo, scaleFactor);
                    case ConditionalEventDefinition _ ->
                        processDiagramCanvas.drawConditionalStartEvent(graphicInfo, scaleFactor);
                    case SignalEventDefinition _ -> processDiagramCanvas.drawSignalStartEvent(graphicInfo, scaleFactor);
                    case MessageEventDefinition _ ->
                        processDiagramCanvas.drawMessageStartEvent(graphicInfo, scaleFactor);
                    case null, default -> processDiagramCanvas.drawNoneStartEvent(graphicInfo);
                }
            } else {
                List<ExtensionElement> eventTypeElements = startEvent.getExtensionElements().get("eventType");
                if (eventTypeElements != null && !eventTypeElements.isEmpty()) {
                    processDiagramCanvas.drawEventRegistryStartEvent(graphicInfo, scaleFactor);

                } else {
                    processDiagramCanvas.drawNoneStartEvent(graphicInfo);
                }
            }
        });

        // signal catch
        activityDrawInstructions.put(IntermediateCatchEvent.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            IntermediateCatchEvent intermediateCatchEvent = (IntermediateCatchEvent) flowNode;
            if (intermediateCatchEvent.getEventDefinitions() != null && !intermediateCatchEvent.getEventDefinitions().isEmpty()) {

                if (intermediateCatchEvent.getEventDefinitions().getFirst() instanceof SignalEventDefinition) {
                    processDiagramCanvas.drawCatchingSignalEvent(flowNode.getName(), graphicInfo, true, scaleFactor);
                } else if (intermediateCatchEvent.getEventDefinitions().getFirst() instanceof TimerEventDefinition) {
                    processDiagramCanvas.drawCatchingTimerEvent(flowNode.getName(), graphicInfo, true, scaleFactor);
                } else if (intermediateCatchEvent.getEventDefinitions().getFirst() instanceof MessageEventDefinition) {
                    processDiagramCanvas.drawCatchingMessageEvent(flowNode.getName(), graphicInfo, true, scaleFactor);
                } else if (intermediateCatchEvent.getEventDefinitions().getFirst() instanceof ConditionalEventDefinition) {
                    processDiagramCanvas.drawCatchingConditionalEvent(flowNode.getName(), graphicInfo, true, scaleFactor);
                }
            }
        });

        // signal throw
        activityDrawInstructions.put(ThrowEvent.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            ThrowEvent throwEvent = (ThrowEvent) flowNode;
            if (throwEvent.getEventDefinitions() != null && !throwEvent.getEventDefinitions().isEmpty()) {
                if (throwEvent.getEventDefinitions().getFirst() instanceof SignalEventDefinition) {
                    processDiagramCanvas.drawThrowingSignalEvent(graphicInfo, scaleFactor);
                } else if (throwEvent.getEventDefinitions().getFirst() instanceof EscalationEventDefinition) {
                    processDiagramCanvas.drawThrowingEscalationEvent(graphicInfo, scaleFactor);
                } else if (throwEvent.getEventDefinitions().getFirst() instanceof CompensateEventDefinition) {
                    processDiagramCanvas.drawThrowingCompensateEvent(graphicInfo, scaleFactor);
                } else {
                    processDiagramCanvas.drawThrowingNoneEvent(graphicInfo, scaleFactor);
                }
            } else {
                processDiagramCanvas.drawThrowingNoneEvent(graphicInfo, scaleFactor);
            }
        });

        // end event
        activityDrawInstructions.put(EndEvent.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            EndEvent endEvent = (EndEvent) flowNode;
            if (endEvent.getEventDefinitions() != null && !endEvent.getEventDefinitions().isEmpty()) {
                if (endEvent.getEventDefinitions().getFirst() instanceof ErrorEventDefinition) {
                    processDiagramCanvas.drawErrorEndEvent(flowNode.getName(), graphicInfo, scaleFactor);
                } else if (endEvent.getEventDefinitions().getFirst() instanceof EscalationEventDefinition) {
                    processDiagramCanvas.drawEscalationEndEvent(flowNode.getName(), graphicInfo, scaleFactor);
                } else {
                    processDiagramCanvas.drawNoneEndEvent(graphicInfo, scaleFactor);
                }
            } else {
                processDiagramCanvas.drawNoneEndEvent(graphicInfo, scaleFactor);
            }
        });

        // task
        activityDrawInstructions.put(Task.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            processDiagramCanvas.drawTask(flowNode.getName(), graphicInfo, scaleFactor);
        });

        // user task
        activityDrawInstructions.put(UserTask.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            processDiagramCanvas.drawUserTask(flowNode.getName(), graphicInfo, scaleFactor);
        });

        // script task
        activityDrawInstructions.put(ScriptTask.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            processDiagramCanvas.drawScriptTask(flowNode.getName(), graphicInfo, scaleFactor);
        });

        // service task
        activityDrawInstructions.put(ServiceTask.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            ServiceTask serviceTask = (ServiceTask) flowNode;
            if ("camel".equalsIgnoreCase(serviceTask.getType())) {
                processDiagramCanvas.drawCamelTask(serviceTask.getName(), graphicInfo, scaleFactor);
            } else if (ServiceTask.HTTP_TASK.equalsIgnoreCase(serviceTask.getType())) {
                processDiagramCanvas.drawHttpTask(serviceTask.getName(), graphicInfo, scaleFactor);
            } else if (ServiceTask.DMN_TASK.equalsIgnoreCase(serviceTask.getType())) {
                processDiagramCanvas.drawDMNTask(serviceTask.getName(), graphicInfo, scaleFactor);
            } else if (ServiceTask.SHELL_TASK.equalsIgnoreCase(serviceTask.getType())) {
                processDiagramCanvas.drawShellTask(serviceTask.getName(), graphicInfo, scaleFactor);
            } else {
                processDiagramCanvas.drawServiceTask(serviceTask.getName(), graphicInfo, scaleFactor);
            }
        });

        // http service task
        activityDrawInstructions.put(HttpServiceTask.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            processDiagramCanvas.drawHttpTask(flowNode.getName(), graphicInfo, scaleFactor);
        });

        // receive task
        activityDrawInstructions.put(ReceiveTask.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            processDiagramCanvas.drawReceiveTask(flowNode.getName(), graphicInfo, scaleFactor);
        });

        // send task
        activityDrawInstructions.put(SendTask.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            processDiagramCanvas.drawSendTask(flowNode.getName(), graphicInfo, scaleFactor);
        });

        // manual task
        activityDrawInstructions.put(ManualTask.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            processDiagramCanvas.drawManualTask(flowNode.getName(), graphicInfo, scaleFactor);
        });

        // send event service task
        activityDrawInstructions.put(SendEventServiceTask.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            processDiagramCanvas.drawSendEventServiceTask(flowNode.getName(), graphicInfo, scaleFactor);
        });

        // external worker service task
        activityDrawInstructions.put(ExternalWorkerServiceTask.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            ServiceTask serviceTask = (ServiceTask) flowNode;
            processDiagramCanvas.drawServiceTask(serviceTask.getName(), graphicInfo, scaleFactor);
        });

        // case service task
        activityDrawInstructions.put(CaseServiceTask.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            processDiagramCanvas.drawCaseServiceTask(flowNode.getName(), graphicInfo, scaleFactor);
        });

        // businessRuleTask task
        activityDrawInstructions.put(BusinessRuleTask.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            processDiagramCanvas.drawBusinessRuleTask(flowNode.getName(), graphicInfo, scaleFactor);
        });

        // exclusive gateway
        activityDrawInstructions.put(ExclusiveGateway.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            processDiagramCanvas.drawExclusiveGateway(graphicInfo, scaleFactor);
        });

        // inclusive gateway
        activityDrawInstructions.put(InclusiveGateway.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            processDiagramCanvas.drawInclusiveGateway(graphicInfo, scaleFactor);
        });

        // parallel gateway
        activityDrawInstructions.put(ParallelGateway.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            processDiagramCanvas.drawParallelGateway(graphicInfo, scaleFactor);
        });

        // event based gateway
        activityDrawInstructions.put(EventGateway.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            processDiagramCanvas.drawEventBasedGateway(graphicInfo, scaleFactor);
        });

        // Boundary timer
        activityDrawInstructions.put(BoundaryEvent.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            BoundaryEvent boundaryEvent = (BoundaryEvent) flowNode;
            if (boundaryEvent.getEventDefinitions() != null && !boundaryEvent.getEventDefinitions().isEmpty()) {
                EventDefinition eventDefinition = boundaryEvent.getEventDefinitions().getFirst();
                if (eventDefinition instanceof TimerEventDefinition) {
                    processDiagramCanvas.drawCatchingTimerEvent(flowNode.getName(), graphicInfo, boundaryEvent.isCancelActivity(), scaleFactor);

                } else if (eventDefinition instanceof ConditionalEventDefinition) {
                    processDiagramCanvas.drawCatchingConditionalEvent(graphicInfo, boundaryEvent.isCancelActivity(), scaleFactor);

                } else if (eventDefinition instanceof ErrorEventDefinition) {
                    processDiagramCanvas.drawCatchingErrorEvent(graphicInfo, boundaryEvent.isCancelActivity(), scaleFactor);

                } else if (eventDefinition instanceof EscalationEventDefinition) {
                    processDiagramCanvas.drawCatchingEscalationEvent(graphicInfo, boundaryEvent.isCancelActivity(), scaleFactor);

                } else if (eventDefinition instanceof SignalEventDefinition) {
                    processDiagramCanvas.drawCatchingSignalEvent(flowNode.getName(), graphicInfo, boundaryEvent.isCancelActivity(), scaleFactor);

                } else if (eventDefinition instanceof MessageEventDefinition) {
                    processDiagramCanvas.drawCatchingMessageEvent(flowNode.getName(), graphicInfo, boundaryEvent.isCancelActivity(), scaleFactor);

                } else if (eventDefinition instanceof CompensateEventDefinition) {
                    processDiagramCanvas.drawCatchingCompensateEvent(graphicInfo, boundaryEvent.isCancelActivity(), scaleFactor);
                }

            } else {
                List<ExtensionElement> eventTypeElements = boundaryEvent.getExtensionElements().get("eventType");
                if (eventTypeElements != null && !eventTypeElements.isEmpty()) {
                    processDiagramCanvas.drawCatchingEventRegistryEvent(flowNode.getName(), graphicInfo, boundaryEvent.isCancelActivity(), scaleFactor);
                }
            }
        });

        // subprocess
        activityDrawInstructions.put(SubProcess.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            if (graphicInfo.getExpanded() != null && !graphicInfo.getExpanded()) {
                processDiagramCanvas.drawCollapsedSubProcess(flowNode.getName(), graphicInfo, false, scaleFactor);
            } else {
                processDiagramCanvas.drawExpandedSubProcess(flowNode.getName(), graphicInfo, false, scaleFactor);
            }
        });

        // transaction
        activityDrawInstructions.put(Transaction.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            if (graphicInfo.getExpanded() != null && !graphicInfo.getExpanded()) {
                processDiagramCanvas.drawCollapsedSubProcess(flowNode.getName(), graphicInfo, false, scaleFactor);
            } else {
                processDiagramCanvas.drawExpandedTransaction(flowNode.getName(), graphicInfo, scaleFactor);
            }
        });

        // Event subprocess
        activityDrawInstructions.put(EventSubProcess.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            if (graphicInfo.getExpanded() != null && !graphicInfo.getExpanded()) {
                processDiagramCanvas.drawCollapsedSubProcess(flowNode.getName(), graphicInfo, true, scaleFactor);
            } else {
                processDiagramCanvas.drawExpandedSubProcess(flowNode.getName(), graphicInfo, true, scaleFactor);
            }
        });

        // Adhoc subprocess
        activityDrawInstructions.put(AdhocSubProcess.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            if (graphicInfo.getExpanded() != null && !graphicInfo.getExpanded()) {
                processDiagramCanvas.drawCollapsedSubProcess(flowNode.getName(), graphicInfo, false, scaleFactor);
            } else {
                processDiagramCanvas.drawExpandedSubProcess(flowNode.getName(), graphicInfo, false, scaleFactor);
            }
        });

        // call activity
        activityDrawInstructions.put(CallActivity.class, (processDiagramCanvas, bpmnModel, flowNode) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            processDiagramCanvas.drawCollapsedCallActivity(flowNode.getName(), graphicInfo, scaleFactor);
        });

        // text annotation
        artifactDrawInstructions.put(TextAnnotation.class, (processDiagramCanvas, bpmnModel, artifact) -> {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(artifact.getId());
            TextAnnotation textAnnotation = (TextAnnotation) artifact;
            processDiagramCanvas.drawTextAnnotation(textAnnotation.getText(), graphicInfo, scaleFactor);
        });

        // association
        artifactDrawInstructions.put(Association.class, (processDiagramCanvas, bpmnModel, artifact) -> {
            Association association = (Association) artifact;
            String sourceRef = association.getSourceRef();
            String targetRef = association.getTargetRef();

            // source and target can be instance of FlowElement or Artifact
            BaseElement sourceElement = bpmnModel.getFlowElement(sourceRef);
            BaseElement targetElement = bpmnModel.getFlowElement(targetRef);
            if (sourceElement == null) {
                sourceElement = bpmnModel.getArtifact(sourceRef);
            }
            if (targetElement == null) {
                targetElement = bpmnModel.getArtifact(targetRef);
            }
            List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(artifact.getId());
            graphicInfoList = connectionPerfectionizer(processDiagramCanvas, bpmnModel, sourceElement, targetElement, graphicInfoList);
            int[] xPoints = new int[graphicInfoList.size()];
            int[] yPoints = new int[graphicInfoList.size()];
            for (int i = 1; i < graphicInfoList.size(); i++) {
                GraphicInfo graphicInfo = graphicInfoList.get(i);
                GraphicInfo previousGraphicInfo = graphicInfoList.get(i - 1);

                if (i == 1) {
                    xPoints[0] = (int) previousGraphicInfo.getX();
                    yPoints[0] = (int) previousGraphicInfo.getY();
                }
                xPoints[i] = (int) graphicInfo.getX();
                yPoints[i] = (int) graphicInfo.getY();
            }

            AssociationDirection associationDirection = association.getAssociationDirection();
            processDiagramCanvas.drawAssociation(xPoints, yPoints, associationDirection, false, scaleFactor);
        });
    }

    /**
     * This method makes coordinates of connection flow better.
     */
    protected static List<GraphicInfo> connectionPerfectionizer(CustomDefaultProcessDiagramCanvas processDiagramCanvas, BpmnModel bpmnModel, BaseElement sourceElement, BaseElement targetElement, List<GraphicInfo> graphicInfoList) {
        GraphicInfo sourceGraphicInfo = bpmnModel.getGraphicInfo(sourceElement.getId());
        GraphicInfo targetGraphicInfo = bpmnModel.getGraphicInfo(targetElement.getId());

        CustomDefaultProcessDiagramCanvas.SHAPE_TYPE sourceShapeType = getShapeType(sourceElement);
        CustomDefaultProcessDiagramCanvas.SHAPE_TYPE targetShapeType = getShapeType(targetElement);

        return processDiagramCanvas.connectionPerfectionizer(sourceShapeType, targetShapeType, sourceGraphicInfo, targetGraphicInfo, graphicInfoList);
    }

    /**
     * This method returns shape type of base element.
     *
     * @param baseElement base element
     * @return CustomDefaultProcessDiagramCanvas.SHAPE_TYPE
     */
    protected static CustomDefaultProcessDiagramCanvas.SHAPE_TYPE getShapeType(BaseElement baseElement) {
        if (baseElement instanceof Activity || baseElement instanceof TextAnnotation) {
            return CustomDefaultProcessDiagramCanvas.SHAPE_TYPE.Rectangle;
        } else if (baseElement instanceof Gateway) {
            return CustomDefaultProcessDiagramCanvas.SHAPE_TYPE.Rhombus;
        } else if (baseElement instanceof Event) {
            return CustomDefaultProcessDiagramCanvas.SHAPE_TYPE.Ellipse;
        }
        return null;
    }

    protected static GraphicInfo getLineCenter(List<GraphicInfo> graphicInfoList) {
        GraphicInfo gi = new GraphicInfo();

        @SuppressWarnings("all")
        int[] xPoints = new int[graphicInfoList.size()];
        @SuppressWarnings("all")
        int[] yPoints = new int[graphicInfoList.size()];

        double length = 0;
        double[] lengths = new double[graphicInfoList.size()];
        lengths[0] = 0;
        double m;
        for (int i = 1; i < graphicInfoList.size(); i++) {
            GraphicInfo graphicInfo = graphicInfoList.get(i);
            GraphicInfo previousGraphicInfo = graphicInfoList.get(i - 1);

            if (i == 1) {
                xPoints[0] = (int) previousGraphicInfo.getX();
                yPoints[0] = (int) previousGraphicInfo.getY();
            }
            xPoints[i] = (int) graphicInfo.getX();
            yPoints[i] = (int) graphicInfo.getY();

            length += Math.sqrt(
                Math.pow((int) graphicInfo.getX() - (int) previousGraphicInfo.getX(), 2) +
                    Math.pow((int) graphicInfo.getY() - (int) previousGraphicInfo.getY(), 2));
            lengths[i] = length;
        }
        m = length / 2;
        int p1 = 0;
        int p2 = 1;
        for (int i = 1; i < lengths.length; i++) {
            double len = lengths[i];
            p1 = i - 1;
            p2 = i;
            if (len > m) {
                break;
            }
        }

        GraphicInfo graphicInfo1 = graphicInfoList.get(p1);
        GraphicInfo graphicInfo2 = graphicInfoList.get(p2);

        double AB = (int) graphicInfo2.getX() - (int) graphicInfo1.getX();
        double OA = (int) graphicInfo2.getY() - (int) graphicInfo1.getY();
        double OB = lengths[p2] - lengths[p1];
        double ob = m - lengths[p1];
        double ab = AB * ob / OB;
        double oa = OA * ob / OB;

        double mx = graphicInfo1.getX() + ab;
        double my = graphicInfo1.getY() + oa;

        gi.setX(mx);
        gi.setY(my);
        return gi;
    }

    private static void drawHighLight(CustomDefaultProcessDiagramCanvas processDiagramCanvas, GraphicInfo graphicInfo) {
        processDiagramCanvas.drawHighLight((int) graphicInfo.getX(), (int) graphicInfo.getY(), (int) graphicInfo.getWidth(), (int) graphicInfo.getHeight());

    }

    private static void drawHighLightRed(CustomDefaultProcessDiagramCanvas processDiagramCanvas, GraphicInfo graphicInfo) {
        processDiagramCanvas.drawHighLightRed((int) graphicInfo.getX(), (int) graphicInfo.getY(), (int) graphicInfo.getWidth(), (int) graphicInfo.getHeight());

    }

    protected static CustomDefaultProcessDiagramCanvas initProcessDiagramCanvas(BpmnModel bpmnModel, String imageType,
                                                                                String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader) {

        // We need to calculate maximum values to know how big the image will be in its entirety
        double minX = Double.MAX_VALUE;
        double maxX = 0;
        double minY = Double.MAX_VALUE;
        double maxY = 0;

        for (Pool pool : bpmnModel.getPools()) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
            minX = graphicInfo.getX();
            maxX = graphicInfo.getX() + graphicInfo.getWidth();
            minY = graphicInfo.getY();
            maxY = graphicInfo.getY() + graphicInfo.getHeight();
        }

        List<FlowNode> flowNodes = gatherAllFlowNodes(bpmnModel);
        for (FlowNode flowNode : flowNodes) {

            GraphicInfo flowNodeGraphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());

            // width
            if (flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth() > maxX) {
                maxX = flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth();
            }
            if (flowNodeGraphicInfo.getX() < minX) {
                minX = flowNodeGraphicInfo.getX();
            }
            // height
            if (flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight() > maxY) {
                maxY = flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight();
            }
            if (flowNodeGraphicInfo.getY() < minY) {
                minY = flowNodeGraphicInfo.getY();
            }

            for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
                List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
                if (graphicInfoList != null) {
                    for (GraphicInfo graphicInfo : graphicInfoList) {
                        // width
                        if (graphicInfo.getX() > maxX) {
                            maxX = graphicInfo.getX();
                        }
                        if (graphicInfo.getX() < minX) {
                            minX = graphicInfo.getX();
                        }
                        // height
                        if (graphicInfo.getY() > maxY) {
                            maxY = graphicInfo.getY();
                        }
                        if (graphicInfo.getY() < minY) {
                            minY = graphicInfo.getY();
                        }
                    }
                }
            }
        }

        List<Artifact> artifacts = gatherAllArtifacts(bpmnModel);
        for (Artifact artifact : artifacts) {

            GraphicInfo artifactGraphicInfo = bpmnModel.getGraphicInfo(artifact.getId());

            if (artifactGraphicInfo != null) {
                // width
                if (artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth() > maxX) {
                    maxX = artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth();
                }
                if (artifactGraphicInfo.getX() < minX) {
                    minX = artifactGraphicInfo.getX();
                }
                // height
                if (artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight() > maxY) {
                    maxY = artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight();
                }
                if (artifactGraphicInfo.getY() < minY) {
                    minY = artifactGraphicInfo.getY();
                }
            }

            List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(artifact.getId());
            if (graphicInfoList != null) {
                for (GraphicInfo graphicInfo : graphicInfoList) {
                    // width
                    if (graphicInfo.getX() > maxX) {
                        maxX = graphicInfo.getX();
                    }
                    if (graphicInfo.getX() < minX) {
                        minX = graphicInfo.getX();
                    }
                    // height
                    if (graphicInfo.getY() > maxY) {
                        maxY = graphicInfo.getY();
                    }
                    if (graphicInfo.getY() < minY) {
                        minY = graphicInfo.getY();
                    }
                }
            }
        }

        int nrOfLanes = 0;
        for (Process process : bpmnModel.getProcesses()) {
            for (Lane l : process.getLanes()) {

                nrOfLanes++;

                GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(l.getId());
                // // width
                if (graphicInfo.getX() + graphicInfo.getWidth() > maxX) {
                    maxX = graphicInfo.getX() + graphicInfo.getWidth();
                }
                if (graphicInfo.getX() < minX) {
                    minX = graphicInfo.getX();
                }
                // height
                if (graphicInfo.getY() + graphicInfo.getHeight() > maxY) {
                    maxY = graphicInfo.getY() + graphicInfo.getHeight();
                }
                if (graphicInfo.getY() < minY) {
                    minY = graphicInfo.getY();
                }
            }
        }

        // Special case, see https://activiti.atlassian.net/browse/ACT-1431
        if (flowNodes.isEmpty() && bpmnModel.getPools().isEmpty() && nrOfLanes == 0) {
            // Nothing to show
            minX = 0;
            minY = 0;
        }

        return new CustomDefaultProcessDiagramCanvas((int) maxX + 10, (int) maxY + 10, (int) minX, (int) minY,
            imageType, activityFontName, labelFontName, annotationFontName, customClassLoader);
    }

    protected static List<Artifact> gatherAllArtifacts(BpmnModel bpmnModel) {
        List<Artifact> artifacts = new ArrayList<>();
        for (Process process : bpmnModel.getProcesses()) {
            artifacts.addAll(process.getArtifacts());
        }
        return artifacts;
    }

    protected static List<FlowNode> gatherAllFlowNodes(BpmnModel bpmnModel) {
        List<FlowNode> flowNodes = new ArrayList<>();
        for (Process process : bpmnModel.getProcesses()) {
            flowNodes.addAll(gatherAllFlowNodes(process));
        }
        return flowNodes;
    }

    protected static List<FlowNode> gatherAllFlowNodes(FlowElementsContainer flowElementsContainer) {
        List<FlowNode> flowNodes = new ArrayList<>();
        for (FlowElement flowElement : flowElementsContainer.getFlowElements()) {
            if (flowElement instanceof FlowNode) {
                flowNodes.add((FlowNode) flowElement);
            }
            if (flowElement instanceof FlowElementsContainer) {
                flowNodes.addAll(gatherAllFlowNodes((FlowElementsContainer) flowElement));
            }
        }
        return flowNodes;
    }

    @Override
    public InputStream generateDiagram(BpmnModel bpmnModel, String imageType, List<String> highLightedActivities, List<String> highLightedFlows,
                                       String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader, double scaleFactor, boolean drawSequenceFlowNameWithNoLabelDI) {

        return generateProcessDiagram(bpmnModel, imageType, highLightedActivities, highLightedFlows,
            activityFontName, labelFontName, annotationFontName, customClassLoader, scaleFactor, drawSequenceFlowNameWithNoLabelDI).generateImage(imageType);
    }

    @Override
    public InputStream generateDiagram(BpmnModel bpmnModel, String imageType, List<String> highLightedActivities, List<String> highLightedFlows, boolean drawSequenceFlowNameWithNoLabelDI) {
        return generateDiagram(bpmnModel, imageType, highLightedActivities, highLightedFlows, null, null, null, null, 1.0, drawSequenceFlowNameWithNoLabelDI);
    }

    @Override
    public InputStream generateDiagram(BpmnModel bpmnModel, String imageType,
                                       List<String> highLightedActivities, List<String> highLightedFlows, double scaleFactor, boolean drawSequenceFlowNameWithNoLabelDI) {
        return generateDiagram(bpmnModel, imageType, highLightedActivities, highLightedFlows, null, null, null, null, scaleFactor, drawSequenceFlowNameWithNoLabelDI);
    }

    @Override
    public InputStream generateDiagram(BpmnModel bpmnModel, String imageType, List<String> highLightedActivities, boolean drawSequenceFlowNameWithNoLabelDI) {
        return generateDiagram(bpmnModel, imageType, highLightedActivities, Collections.emptyList(), drawSequenceFlowNameWithNoLabelDI);
    }

    @Override
    public InputStream generateDiagram(BpmnModel bpmnModel, String imageType, List<String> highLightedActivities, double scaleFactor, boolean drawSequenceFlowNameWithNoLabelDI) {
        return generateDiagram(bpmnModel, imageType, highLightedActivities, Collections.emptyList(), scaleFactor, drawSequenceFlowNameWithNoLabelDI);
    }

    @Override
    public InputStream generateDiagram(BpmnModel bpmnModel, String imageType, String activityFontName,
                                       String labelFontName, String annotationFontName, ClassLoader customClassLoader, boolean drawSequenceFlowNameWithNoLabelDI) {

        return generateDiagram(bpmnModel, imageType, Collections.emptyList(), Collections.emptyList(),
            activityFontName, labelFontName, annotationFontName, customClassLoader, 1.0, drawSequenceFlowNameWithNoLabelDI);
    }

    @Override
    public InputStream generateDiagram(BpmnModel bpmnModel, String imageType, String activityFontName,
                                       String labelFontName, String annotationFontName, ClassLoader customClassLoader, double scaleFactor, boolean drawSequenceFlowNameWithNoLabelDI) {

        return generateDiagram(bpmnModel, imageType, Collections.emptyList(), Collections.emptyList(),
            activityFontName, labelFontName, annotationFontName, customClassLoader, scaleFactor, drawSequenceFlowNameWithNoLabelDI);
    }

    @Override
    public InputStream generatePngDiagram(BpmnModel bpmnModel, boolean drawSequenceFlowNameWithNoLabelDI) {
        return generatePngDiagram(bpmnModel, 1.0, drawSequenceFlowNameWithNoLabelDI);
    }

    @Override
    public InputStream generatePngDiagram(BpmnModel bpmnModel, double scaleFactor, boolean drawSequenceFlowNameWithNoLabelDI) {
        return generateDiagram(bpmnModel, "png", Collections.emptyList(), Collections.emptyList(), scaleFactor, drawSequenceFlowNameWithNoLabelDI);
    }

    @Override
    public InputStream generateJpgDiagram(BpmnModel bpmnModel) {
        return generateJpgDiagram(bpmnModel, 1.0, false);
    }

    @Override
    public InputStream generateJpgDiagram(BpmnModel bpmnModel, double scaleFactor, boolean drawSequenceFlowNameWithNoLabelDI) {
        return generateDiagram(bpmnModel, "jpg", Collections.emptyList(), Collections.emptyList(), drawSequenceFlowNameWithNoLabelDI);
    }

    public BufferedImage generateImage(BpmnModel bpmnModel, String imageType, List<String> highLightedActivities, List<String> highLightedFlows,
                                       String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader, double scaleFactor, boolean drawSequenceFlowNameWithNoLabelDI) {

        return generateProcessDiagram(bpmnModel, imageType, highLightedActivities, highLightedFlows,
            activityFontName, labelFontName, annotationFontName, customClassLoader, scaleFactor, drawSequenceFlowNameWithNoLabelDI).generateBufferedImage(imageType);
    }

    public BufferedImage generateImage(BpmnModel bpmnModel, String imageType,
                                       List<String> highLightedActivities, List<String> highLightedFlows, double scaleFactor, boolean drawSequenceFlowNameWithNoLabelDI) {

        return generateImage(bpmnModel, imageType, highLightedActivities, highLightedFlows, null, null, null, null, scaleFactor, drawSequenceFlowNameWithNoLabelDI);
    }

    @Override
    public BufferedImage generatePngImage(BpmnModel bpmnModel, double scaleFactor) {
        return generateImage(bpmnModel, "png", Collections.emptyList(), Collections.emptyList(), scaleFactor, false);
    }

    protected CustomDefaultProcessDiagramCanvas generateProcessDiagram(BpmnModel bpmnModel, String imageType,
                                                                       List<String> highLightedActivities, List<String> highLightedFlows,
                                                                       String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader, double scaleFactor, boolean drawSequenceFlowNameWithNoLabelDI) {

        prepareBpmnModel(bpmnModel);

        CustomDefaultProcessDiagramCanvas processDiagramCanvas = initProcessDiagramCanvas(bpmnModel, imageType, activityFontName, labelFontName, annotationFontName, customClassLoader);

        // Draw pool shape, if process is participant in collaboration
        for (Pool pool : bpmnModel.getPools()) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
            processDiagramCanvas.drawPoolOrLane(pool.getName(), graphicInfo, scaleFactor);
        }

        // Draw lanes
        for (Process process : bpmnModel.getProcesses()) {
            for (Lane lane : process.getLanes()) {
                GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(lane.getId());
                processDiagramCanvas.drawPoolOrLane(lane.getName(), graphicInfo, scaleFactor);
            }
        }

        // Draw activities and their sequence-flows
        for (Process process : bpmnModel.getProcesses()) {
            for (FlowNode flowNode : process.findFlowElementsOfType(FlowNode.class)) {
                if (!isPartOfCollapsedSubProcess(flowNode, bpmnModel)) {
                    drawActivity(processDiagramCanvas, bpmnModel, flowNode, highLightedActivities, highLightedFlows, scaleFactor, drawSequenceFlowNameWithNoLabelDI);
                }
            }
        }

        // Draw artifacts
        for (Process process : bpmnModel.getProcesses()) {

            for (Artifact artifact : process.getArtifacts()) {
                drawArtifact(processDiagramCanvas, bpmnModel, artifact);
            }

            List<SubProcess> subProcesses = process.findFlowElementsOfType(SubProcess.class, true);
            if (subProcesses != null) {
                for (SubProcess subProcess : subProcesses) {

                    GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(subProcess.getId());
                    if (graphicInfo != null && graphicInfo.getExpanded() != null && !graphicInfo.getExpanded()) {
                        continue;
                    }

                    if (!isPartOfCollapsedSubProcess(subProcess, bpmnModel)) {
                        for (Artifact subProcessArtifact : subProcess.getArtifacts()) {
                            drawArtifact(processDiagramCanvas, bpmnModel, subProcessArtifact);
                        }
                    }
                }
            }
        }

        return processDiagramCanvas;
    }

    protected void prepareBpmnModel(BpmnModel bpmnModel) {

        // Need to make sure all elements have positive x and y.
        // Check all graphicInfo and update the elements accordingly

        List<GraphicInfo> allGraphicInfos = new ArrayList<>();
        if (bpmnModel.getLocationMap() != null) {
            allGraphicInfos.addAll(bpmnModel.getLocationMap().values());
        }
        if (bpmnModel.getLabelLocationMap() != null) {
            allGraphicInfos.addAll(bpmnModel.getLabelLocationMap().values());
        }
        if (bpmnModel.getFlowLocationMap() != null) {
            for (List<GraphicInfo> flowGraphicInfos : bpmnModel.getFlowLocationMap().values()) {
                allGraphicInfos.addAll(flowGraphicInfos);
            }
        }

        if (!allGraphicInfos.isEmpty()) {

            boolean needsTranslationX = false;
            boolean needsTranslationY = false;

            double lowestX = 0.0;
            double lowestY = 0.0;

            // Collect lowest x and y
            for (GraphicInfo graphicInfo : allGraphicInfos) {

                double x = graphicInfo.getX();
                double y = graphicInfo.getY();

                if (x < lowestX) {
                    needsTranslationX = true;
                    lowestX = x;
                }
                if (y < lowestY) {
                    needsTranslationY = true;
                    lowestY = y;
                }

            }

            // Update all graphicInfo objects
            if (needsTranslationX || needsTranslationY) {

                double translationX = Math.abs(lowestX);
                double translationY = Math.abs(lowestY);

                for (GraphicInfo graphicInfo : allGraphicInfos) {
                    if (needsTranslationX) {
                        graphicInfo.setX(graphicInfo.getX() + translationX);
                    }
                    if (needsTranslationY) {
                        graphicInfo.setY(graphicInfo.getY() + translationY);
                    }
                }
            }

        }

    }

    protected void drawActivity(CustomDefaultProcessDiagramCanvas processDiagramCanvas, BpmnModel bpmnModel,
                                FlowNode flowNode, List<String> highLightedActivities, List<String> highLightedFlows, double scaleFactor, Boolean drawSequenceFlowNameWithNoLabelDI) {

        ActivityDrawInstruction drawInstruction = activityDrawInstructions.get(flowNode.getClass());
        if (drawInstruction != null) {

            drawInstruction.draw(processDiagramCanvas, bpmnModel, flowNode);

            // Gather info on the multi instance marker
            boolean multiInstanceSequential = false;
            boolean multiInstanceParallel = false;
            boolean collapsed = false;
            if (flowNode instanceof Activity activity) {
                MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = activity.getLoopCharacteristics();
                if (multiInstanceLoopCharacteristics != null) {
                    multiInstanceSequential = multiInstanceLoopCharacteristics.isSequential();
                    multiInstanceParallel = !multiInstanceSequential;
                }
            }

            // Gather info on the collapsed marker
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            if (flowNode instanceof SubProcess) {
                collapsed = graphicInfo.getExpanded() != null && !graphicInfo.getExpanded();
            } else if (flowNode instanceof CallActivity) {
                collapsed = true;
            }

            if (scaleFactor == 1.0) {
                // Actually draw the markers
                processDiagramCanvas.drawActivityMarkers((int) graphicInfo.getX(), (int) graphicInfo.getY(), (int) graphicInfo.getWidth(), (int) graphicInfo.getHeight(),
                    multiInstanceSequential, multiInstanceParallel, collapsed);
            }

            // Draw highlighted activities
            if (highLightedActivities.contains(flowNode.getId())) {
                drawHighLightRed(processDiagramCanvas, bpmnModel.getGraphicInfo(flowNode.getId()));
            } else if (highLightedActivities.contains(Color.RED.toString() + flowNode.getId())) {
                drawHighLight(processDiagramCanvas, bpmnModel.getGraphicInfo(flowNode.getId()));
            }

        } else if (flowNode instanceof Task) {
            activityDrawInstructions.get(Task.class).draw(processDiagramCanvas, bpmnModel, flowNode);

            if (highLightedActivities.contains(flowNode.getId())) {
                drawHighLightRed(processDiagramCanvas, bpmnModel.getGraphicInfo(flowNode.getId()));
            } else if (highLightedActivities.contains(Color.RED.toString() + flowNode.getId())) {
                drawHighLight(processDiagramCanvas, bpmnModel.getGraphicInfo(flowNode.getId()));
            }
        }

        // Outgoing transitions of activity
        for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
            boolean highLighted = (highLightedFlows.contains(sequenceFlow.getId()));
            String defaultFlow = null;
            if (flowNode instanceof Activity) {
                defaultFlow = ((Activity) flowNode).getDefaultFlow();
            } else if (flowNode instanceof Gateway) {
                defaultFlow = ((Gateway) flowNode).getDefaultFlow();
            }

            boolean isDefault = defaultFlow != null && defaultFlow.equalsIgnoreCase(sequenceFlow.getId());
            boolean drawConditionalIndicator = sequenceFlow.getConditionExpression() != null && !sequenceFlow.getConditionExpression().trim().isEmpty() && !(flowNode instanceof Gateway);

            String sourceRef = sequenceFlow.getSourceRef();
            String targetRef = sequenceFlow.getTargetRef();
            FlowElement sourceElement = bpmnModel.getFlowElement(sourceRef);
            FlowElement targetElement = bpmnModel.getFlowElement(targetRef);
            List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
            if (graphicInfoList != null && !graphicInfoList.isEmpty()) {
                graphicInfoList = connectionPerfectionizer(processDiagramCanvas, bpmnModel, sourceElement, targetElement, graphicInfoList);
                int[] xPoints = new int[graphicInfoList.size()];
                int[] yPoints = new int[graphicInfoList.size()];

                for (int i = 1; i < graphicInfoList.size(); i++) {
                    GraphicInfo graphicInfo = graphicInfoList.get(i);
                    GraphicInfo previousGraphicInfo = graphicInfoList.get(i - 1);

                    if (i == 1) {
                        xPoints[0] = (int) previousGraphicInfo.getX();
                        yPoints[0] = (int) previousGraphicInfo.getY();
                    }
                    xPoints[i] = (int) graphicInfo.getX();
                    yPoints[i] = (int) graphicInfo.getY();

                }

                processDiagramCanvas.drawSequenceflow(xPoints, yPoints, drawConditionalIndicator, isDefault, highLighted, scaleFactor);

                // Draw sequenceflow label
                GraphicInfo labelGraphicInfo = bpmnModel.getLabelGraphicInfo(sequenceFlow.getId());
                if (labelGraphicInfo != null) {
                    processDiagramCanvas.drawLabel(sequenceFlow.getName(), labelGraphicInfo, false);
                } else {
                    if (drawSequenceFlowNameWithNoLabelDI) {
                        GraphicInfo lineCenter = getLineCenter(graphicInfoList);
                        processDiagramCanvas.drawLabel(sequenceFlow.getName(), lineCenter, false);
                    }

                }
            }
        }

        // Nested elements
        if (flowNode instanceof FlowElementsContainer) {
            for (FlowElement nestedFlowElement : ((FlowElementsContainer) flowNode).getFlowElements()) {
                if (nestedFlowElement instanceof FlowNode && !isPartOfCollapsedSubProcess(nestedFlowElement, bpmnModel)) {
                    drawActivity(processDiagramCanvas, bpmnModel, (FlowNode) nestedFlowElement,
                        highLightedActivities, highLightedFlows, scaleFactor, drawSequenceFlowNameWithNoLabelDI);
                }
            }
        }
    }

    protected void drawArtifact(CustomDefaultProcessDiagramCanvas processDiagramCanvas, BpmnModel bpmnModel, Artifact artifact) {

        ArtifactDrawInstruction drawInstruction = artifactDrawInstructions.get(artifact.getClass());
        if (drawInstruction != null) {
            drawInstruction.draw(processDiagramCanvas, bpmnModel, artifact);
        }
    }

    protected boolean isPartOfCollapsedSubProcess(FlowElement flowElement, BpmnModel model) {
        SubProcess subProcess = flowElement.getSubProcess();
        if (subProcess != null) {
            GraphicInfo graphicInfo = model.getGraphicInfo(subProcess.getId());
            if (graphicInfo != null && graphicInfo.getExpanded() != null && !graphicInfo.getExpanded()) {
                return true;
            }

            return isPartOfCollapsedSubProcess(subProcess, model);
        }

        return false;
    }

    protected interface ActivityDrawInstruction {
        void draw(CustomDefaultProcessDiagramCanvas processDiagramCanvas, BpmnModel bpmnModel, FlowNode flowNode);
    }

    protected interface ArtifactDrawInstruction {
        void draw(CustomDefaultProcessDiagramCanvas processDiagramCanvas, BpmnModel bpmnModel, Artifact artifact);
    }
}