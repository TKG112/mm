#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D BlurSampler;
uniform sampler2D MaskSampler;
uniform sampler2D SceneDepthSampler;
uniform float RenderMode;
uniform float UseSourceColor;
uniform vec4  OutlineColor;
uniform float ThermalPalette;
uniform float DetailStrength;
uniform float HandCull;

in vec2 texCoord;
out vec4 fragColor;

const vec3 LUMA = vec3(0.299, 0.587, 0.114);

vec3 thermalPalette(float palette, float heat) {
    heat = clamp(heat, 0.0, 1.0);

    if (palette < 0.5) {
        return vec3(heat);
    } else if (palette < 1.5) {
        return vec3(1.0 - heat);
    } else if (palette < 2.5) {
        vec3 c = vec3(0.0);
        c = mix(c, vec3(0.55, 0.00, 0.00), smoothstep(0.00, 0.35, heat));
        c = mix(c, vec3(1.00, 0.35, 0.00), smoothstep(0.35, 0.60, heat));
        c = mix(c, vec3(1.00, 0.85, 0.10), smoothstep(0.60, 0.85, heat));
        c = mix(c, vec3(1.00, 1.00, 0.92), smoothstep(0.85, 1.00, heat));
        return c;
    } else if (palette < 3.5) {
        vec3 c = vec3(0.06, 0.00, 0.32);
        c = mix(c, vec3(0.42, 0.00, 0.60), smoothstep(0.00, 0.28, heat));
        c = mix(c, vec3(0.85, 0.08, 0.35), smoothstep(0.28, 0.52, heat));
        c = mix(c, vec3(1.00, 0.45, 0.05), smoothstep(0.52, 0.78, heat));
        c = mix(c, vec3(1.00, 0.95, 0.30), smoothstep(0.78, 1.00, heat));
        return c;
    } else {
        vec3 c = vec3(0.10, 0.00, 0.22);
        c = mix(c, vec3(0.55, 0.00, 0.45), smoothstep(0.00, 0.22, heat));
        c = mix(c, vec3(0.90, 0.10, 0.12), smoothstep(0.22, 0.45, heat));
        c = mix(c, vec3(1.00, 0.50, 0.00), smoothstep(0.45, 0.68, heat));
        c = mix(c, vec3(1.00, 0.90, 0.20), smoothstep(0.68, 0.90, heat));
        c = mix(c, vec3(1.00, 1.00, 1.00), smoothstep(0.90, 1.00, heat));
        return c;
    }
}

float heatFromColor(vec3 rgb) {
    float lum = dot(clamp(rgb, 0.0, 1.0), LUMA);
    return clamp(mix(1.0, pow(lum, 0.75), clamp(DetailStrength, 0.0, 1.0)), 0.0, 1.0);
}

void main() {
    vec4 scene = texture(DiffuseSampler, texCoord);

    vec4 result;
    if (RenderMode > 1.5) {
        vec4 mask = texture(MaskSampler, texCoord);
        vec3 color = UseSourceColor > 0.5
            ? mask.rgb
            : thermalPalette(ThermalPalette, heatFromColor(mask.rgb));
        result = vec4(color, clamp(mask.a * OutlineColor.a, 0.0, 1.0));
    } else {
        vec4 blur = texture(BlurSampler, texCoord);
        vec4 mask = texture(MaskSampler, texCoord);
        float outlineAlpha = clamp(blur.a - mask.a, 0.0, 1.0);
        outlineAlpha = smoothstep(0.05, 0.15, outlineAlpha);
        vec3 outColor = UseSourceColor > 0.5
            ? blur.rgb
            : thermalPalette(ThermalPalette, heatFromColor(blur.rgb));
        result = vec4(outColor, clamp(outlineAlpha * OutlineColor.a, 0.0, 1.0));
    }

    vec3 c = clamp(result.rgb, 0.0, 1.0);
    float a = clamp(result.a, 0.0, 1.0);
    if (any(isnan(c)) || any(isinf(c))) c = vec3(0.0);
    if (a != a || isinf(a)) a = 0.0;

    if (HandCull > 0.5 && texture(SceneDepthSampler, texCoord).r < 0.99) {
        a = 0.0;
    }

    vec3 composited = mix(scene.rgb, c, a);
    fragColor = vec4(composited, scene.a);
}
