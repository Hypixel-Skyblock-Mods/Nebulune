package foo.starred.nebulune;

import com.google.gson.JsonParser;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/** Checks runtime mixin contracts against the actual published dependencies. */
public class AthenCompatibilityTest {
    @Test
    public void everyMixinTargetsExistingMembersAndInjectionPoints() throws IOException {
        var loader = getClass().getClassLoader();
        try (var input = loader.getResourceAsStream("nebulune.mixins.json")) {
            assertNotNull("Generated mixin configuration", input);
            var config = JsonParser.parseString(new String(input.readAllBytes(), StandardCharsets.UTF_8)).getAsJsonObject();
            var prefix = config.get("package").getAsString().replace('.', '/') + "/";
            var mixins = config.getAsJsonArray("client");
            assertTrue("Client mixins must be included in the release", mixins.size() >= 8);
            for (var entry : mixins) checkMixin(read(prefix + entry.getAsString().replace('.', '/')));
        }
    }

    private void checkMixin(ClassNode mixin) throws IOException {
        var annotation = annotations(mixin.visibleAnnotations, mixin.invisibleAnnotations).stream()
            .filter(a -> a.desc.equals("Lorg/spongepowered/asm/mixin/Mixin;")).findFirst().orElseThrow();
        var targets = new ArrayList<String>();
        for (var value : list(value(annotation, "value"))) targets.add(((Type) value).getInternalName());
        for (var value : list(value(annotation, "targets"))) targets.add(value.toString().replace('.', '/'));
        assertFalse(mixin.name + " has a target", targets.isEmpty());
        for (var name : targets) {
            var target = read(name);
            for (var field : mixin.fields) {
                if (has(annotations(field.visibleAnnotations, field.invisibleAnnotations), "Shadow"))
                    assertTrue(mixin.name + " shadows missing field " + field.name, target.fields.stream()
                        .anyMatch(f -> f.name.equals(field.name) && f.desc.equals(field.desc)));
            }
            for (var method : mixin.methods) {
                var annotations = annotations(method.visibleAnnotations, method.invisibleAnnotations);
                if (has(annotations, "Shadow"))
                    assertTrue(mixin.name + " shadows missing method " + method.name + method.desc,
                        target.methods.stream().anyMatch(m -> m.name.equals(method.name) && m.desc.equals(method.desc)));
                for (var injection : annotations) {
                    var selectors = value(injection, "method");
                    if (selectors == null) continue;
                    for (var selector : list(selectors)) {
                        var methods = target.methods.stream().filter(m -> matches(selector.toString(), m)).toList();
                        assertFalse(mixin.name + " cannot inject into " + selector + " on " + name, methods.isEmpty());
                        for (var at : list(value(injection, "at"))) {
                            if (!(at instanceof AnnotationNode point)) continue;
                            if (!"INVOKE".equals(value(point, "value"))) continue;
                            var invocation = value(point, "target").toString();
                            assertTrue(mixin.name + " missing invocation " + invocation + " in " + selector,
                                methods.stream().anyMatch(m -> invokes(m, invocation)));
                        }
                    }
                }
            }
        }
    }

    private static boolean invokes(MethodNode method, String target) {
        for (var instruction : method.instructions) {
            if (instruction instanceof MethodInsnNode call &&
                target.equals("L" + call.owner + ";" + call.name + call.desc)) return true;
        }
        return false;
    }

    private static boolean matches(String selector, MethodNode method) {
        if (selector.contains("(")) return selector.equals(method.name + method.desc);
        if (selector.endsWith("*")) return method.name.startsWith(selector.substring(0, selector.length() - 1));
        return selector.equals(method.name);
    }

    private ClassNode read(String name) throws IOException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(name + ".class")) {
            assertNotNull("Missing runtime class " + name, input);
            var node = new ClassNode();
            new ClassReader(input).accept(node, 0);
            return node;
        }
    }

    private static Object value(AnnotationNode annotation, String key) {
        if (annotation.values != null)
            for (int index = 0; index < annotation.values.size(); index += 2)
                if (key.equals(annotation.values.get(index))) return annotation.values.get(index + 1);
        return null;
    }

    private static List<?> list(Object value) {
        if (value == null) return List.of();
        return value instanceof List<?> list ? list : List.of(value);
    }

    private static boolean has(List<AnnotationNode> annotations, String name) {
        return annotations.stream().anyMatch(a -> a.desc.endsWith("/" + name + ";"));
    }

    private static List<AnnotationNode> annotations(List<AnnotationNode> visible, List<AnnotationNode> invisible) {
        var result = new ArrayList<AnnotationNode>();
        if (visible != null) result.addAll(visible);
        if (invisible != null) result.addAll(invisible);
        return result;
    }
}
