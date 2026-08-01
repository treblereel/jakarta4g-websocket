package org.treblereel.gwt.websocket.apt;

import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;

import org.treblereel.gwt.websocket.apt.definition.WebSocketEndpointDefinition;
import org.treblereel.gwt.websocket.apt.generator.EndpointGenerator;

@AutoService(javax.annotation.processing.Processor.class)
public class WebSocketProcessorEntry extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private EndpointGenerator generator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
        this.generator = new EndpointGenerator(filer, messager);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton("jakarta.websocket.ClientEndpoint");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }

        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                if (element instanceof TypeElement) {
                    processEndpoint((TypeElement) element);
                }
            }
        }

        return false;
    }

    private void processEndpoint(TypeElement element) {
        try {
            jakarta.websocket.ClientEndpoint ann =
                    element.getAnnotation(jakarta.websocket.ClientEndpoint.class);
            String[] subprotocols = ann != null ? ann.subprotocols() : new String[0];

            WebSocketEndpointDefinition definition =
                    new WebSocketEndpointDefinition(element, subprotocols);

            for (Element enclosed : element.getEnclosedElements()) {
                if (enclosed instanceof ExecutableElement) {
                    ExecutableElement method = (ExecutableElement) enclosed;
                    if (method.getAnnotation(jakarta.websocket.OnOpen.class) != null) {
                        definition.setOnOpenMethod(method);
                    }
                    if (method.getAnnotation(jakarta.websocket.OnClose.class) != null) {
                        definition.setOnCloseMethod(method);
                    }
                    if (method.getAnnotation(jakarta.websocket.OnMessage.class) != null) {
                        definition.setOnMessageMethod(method);
                    }
                    if (method.getAnnotation(jakarta.websocket.OnError.class) != null) {
                        definition.setOnErrorMethod(method);
                    }
                }
            }

            generator.generate(definition);

            messager.printMessage(Diagnostic.Kind.NOTE,
                    "Generated WebSocket endpoint for "
                            + element.getQualifiedName());
        } catch (Exception e) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    "Error processing " + element.getQualifiedName()
                            + ": " + e.getMessage(),
                    element);
        }
    }
}
