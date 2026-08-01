package org.treblereel.gwt.websocket.apt.definition;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

public class WebSocketEndpointDefinition {

    private final TypeElement typeElement;
    private final String[] subprotocols;
    private ExecutableElement onOpenMethod;
    private ExecutableElement onCloseMethod;
    private ExecutableElement onMessageMethod;
    private ExecutableElement onErrorMethod;

    public WebSocketEndpointDefinition(TypeElement typeElement, String[] subprotocols) {
        this.typeElement = typeElement;
        this.subprotocols = subprotocols;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public String[] getSubprotocols() {
        return subprotocols;
    }

    public String getPackageName() {
        String qualifiedName = typeElement.getQualifiedName().toString();
        int lastDot = qualifiedName.lastIndexOf('.');
        return lastDot > 0 ? qualifiedName.substring(0, lastDot) : "";
    }

    public String getSimpleName() {
        return typeElement.getSimpleName().toString();
    }

    public String getQualifiedName() {
        return typeElement.getQualifiedName().toString();
    }

    public ExecutableElement getOnOpenMethod() {
        return onOpenMethod;
    }

    public void setOnOpenMethod(ExecutableElement onOpenMethod) {
        this.onOpenMethod = onOpenMethod;
    }

    public ExecutableElement getOnCloseMethod() {
        return onCloseMethod;
    }

    public void setOnCloseMethod(ExecutableElement onCloseMethod) {
        this.onCloseMethod = onCloseMethod;
    }

    public ExecutableElement getOnMessageMethod() {
        return onMessageMethod;
    }

    public void setOnMessageMethod(ExecutableElement onMessageMethod) {
        this.onMessageMethod = onMessageMethod;
    }

    public ExecutableElement getOnErrorMethod() {
        return onErrorMethod;
    }

    public void setOnErrorMethod(ExecutableElement onErrorMethod) {
        this.onErrorMethod = onErrorMethod;
    }
}
