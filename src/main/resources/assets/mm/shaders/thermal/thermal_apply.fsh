#version 150
uniform sampler2D BlurSampler;
uniform sampler2D MaskSampler;
uniform float RenderMode;
uniform float UseSourceColor;
uniform vec4  OutlineColor;
in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 result;

    if (RenderMode > 1.5) {
        vec4 mask = texture(MaskSampler, texCoord);
        vec3 color = UseSourceColor > 0.5 ? mask.rgb : OutlineColor.rgb;
        result = vec4(color, clamp(mask.a * OutlineColor.a, 0.0, 1.0));
    } else {
        vec4 blur = texture(BlurSampler, texCoord);
        vec4 mask = texture(MaskSampler, texCoord);
        float outlineAlpha = clamp(blur.a - mask.a, 0.0, 1.0);
        outlineAlpha = smoothstep(0.05, 0.15, outlineAlpha);
        vec3 outColor = UseSourceColor > 0.5 ? blur.rgb : OutlineColor.rgb;
        result = vec4(outColor, clamp(outlineAlpha * OutlineColor.a, 0.0, 1.0));
    }

    vec3 c = clamp(result.rgb, 0.0, 1.0);
    float a = clamp(result.a, 0.0, 1.0);
    if (any(isnan(c)) || any(isinf(c))) c = vec3(0.0);
    if (a != a || isinf(a)) a = 0.0;
    fragColor = vec4(c, a);
}
