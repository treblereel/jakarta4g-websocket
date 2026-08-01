/*
 * Copyright © 2024 Treblereel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.treblereel.gwt.websocket.apt.generator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.treblereel.gwt.websocket.apt.definition.WebSocketEndpointDefinition;

public class EndpointGenerator {

    private final Filer filer;
    private final Messager messager;

    public EndpointGenerator(Filer filer, Messager messager) {
        this.filer = filer;
        this.messager = messager;
    }

    public void generate(WebSocketEndpointDefinition definition) {
        String packageName = definition.getPackageName();
        String simpleName = definition.getSimpleName();
        String generatedName = simpleName + "_WebSocketEndpoint";
        String qualifiedName = packageName.isEmpty()
                ? generatedName : packageName + "." + generatedName;

        try {
            JavaFileObject sourceFile = filer.createSourceFile(qualifiedName);
            try (PrintWriter out = new PrintWriter(sourceFile.openWriter())) {
                if (!packageName.isEmpty()) {
                    out.println("package " + packageName + ";");
                    out.println();
                }

                out.println("import jakarta.websocket.CloseReason;");
                out.println("import jakarta.websocket.Session;");
                out.println(
                        "import org.treblereel.gwt.websocket.client.WebSocketConfig;");
                out.println(
                        "import org.treblereel.gwt.websocket.client.proxy.AbstractWebSocketEndpoint;");
                out.println();

                out.println("public class " + generatedName
                        + " extends AbstractWebSocketEndpoint {");
                out.println();
                out.println("    private final " + simpleName + " delegate;");
                out.println();

                out.println("    public " + generatedName + "("
                        + simpleName + " delegate, WebSocketConfig config) {");
                out.println("        super(config);");
                out.println("        this.delegate = delegate;");
                out.println("    }");

                generateHandleOpen(out, definition);
                generateHandleMessage(out, definition);
                generateHandleClose(out, definition);
                generateHandleError(out, definition);

                out.println("}");
            }
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    "Failed to generate " + generatedName + ": " + e.getMessage());
        }
    }

    private void generateHandleOpen(PrintWriter out,
            WebSocketEndpointDefinition definition) {
        ExecutableElement method = definition.getOnOpenMethod();
        if (method != null) {
            out.println();
            out.println("    @Override");
            out.println("    protected void handleOpen("
                    + "Session session) {");
            out.println("        delegate." + method.getSimpleName()
                    + "(" + buildArgs(method, "session", null) + ");");
            out.println("    }");
        }
    }

    private void generateHandleMessage(PrintWriter out,
            WebSocketEndpointDefinition definition) {
        ExecutableElement method = definition.getOnMessageMethod();
        if (method != null) {
            boolean isBinary = hasByteArrayParam(method);
            out.println();
            out.println("    @Override");
            if (isBinary) {
                out.println("    protected void handleBinaryMessage("
                        + "byte[] data, Session session) {");
                out.println("        delegate." + method.getSimpleName()
                        + "(" + buildBinaryArgs(method) + ");");
            } else {
                out.println("    protected void handleTextMessage("
                        + "String message, Session session) {");
                out.println("        delegate." + method.getSimpleName()
                        + "(" + buildArgs(method, "session", "message") + ");");
            }
            out.println("    }");
        }
    }

    private void generateHandleClose(PrintWriter out,
            WebSocketEndpointDefinition definition) {
        ExecutableElement method = definition.getOnCloseMethod();
        if (method != null) {
            out.println();
            out.println("    @Override");
            out.println("    protected void handleClose("
                    + "CloseReason closeReason, "
                    + "Session session) {");
            out.println("        delegate." + method.getSimpleName()
                    + "(" + buildCloseArgs(method) + ");");
            out.println("    }");
        }
    }

    private void generateHandleError(PrintWriter out,
            WebSocketEndpointDefinition definition) {
        ExecutableElement method = definition.getOnErrorMethod();
        if (method != null) {
            out.println();
            out.println("    @Override");
            out.println("    protected void handleError("
                    + "Throwable error, "
                    + "Session session) {");
            out.println("        delegate." + method.getSimpleName()
                    + "(" + buildErrorArgs(method) + ");");
            out.println("    }");
        }
    }

    private boolean hasByteArrayParam(ExecutableElement method) {
        for (VariableElement param : method.getParameters()) {
            if (param.asType().toString().equals("byte[]")) {
                return true;
            }
        }
        return false;
    }

    private String buildArgs(ExecutableElement method,
            String sessionVar, String messageVar) {
        List<? extends VariableElement> params = method.getParameters();
        if (params.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            String typeName = params.get(i).asType().toString();
            if (isSessionType(typeName)) {
                sb.append("session");
            } else if (isStringType(typeName) && messageVar != null) {
                sb.append(messageVar);
            } else {
                sb.append("null");
            }
        }
        return sb.toString();
    }

    private String buildBinaryArgs(ExecutableElement method) {
        List<? extends VariableElement> params = method.getParameters();
        if (params.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            String typeName = params.get(i).asType().toString();
            if (typeName.equals("byte[]")) {
                sb.append("data");
            } else if (isSessionType(typeName)) {
                sb.append("session");
            } else {
                sb.append("null");
            }
        }
        return sb.toString();
    }

    private String buildCloseArgs(ExecutableElement method) {
        List<? extends VariableElement> params = method.getParameters();
        if (params.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            String typeName = params.get(i).asType().toString();
            if (isCloseReasonType(typeName)) {
                sb.append("closeReason");
            } else if (isSessionType(typeName)) {
                sb.append("session");
            } else {
                sb.append("null");
            }
        }
        return sb.toString();
    }

    private String buildErrorArgs(ExecutableElement method) {
        List<? extends VariableElement> params = method.getParameters();
        if (params.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            String typeName = params.get(i).asType().toString();
            if (isThrowableType(typeName)) {
                sb.append("error");
            } else if (isSessionType(typeName)) {
                sb.append("session");
            } else {
                sb.append("null");
            }
        }
        return sb.toString();
    }

    private boolean isSessionType(String typeName) {
        return typeName.equals("jakarta.websocket.Session")
                || typeName.endsWith(".Session");
    }

    private boolean isStringType(String typeName) {
        return typeName.equals("java.lang.String") || typeName.equals("String");
    }

    private boolean isCloseReasonType(String typeName) {
        return typeName.equals("jakarta.websocket.CloseReason")
                || typeName.endsWith("CloseReason");
    }

    private boolean isThrowableType(String typeName) {
        return typeName.equals("java.lang.Throwable")
                || typeName.endsWith("Throwable")
                || typeName.equals("java.lang.Exception")
                || typeName.endsWith("Exception");
    }
}
