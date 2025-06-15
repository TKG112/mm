#version 120

uniform float NightVisionEnabled;
uniform float VignetteEnabled;
uniform float VignetteRadius;
uniform float Brightness;
uniform float SepiaRatio;
uniform sampler2D DiffuseSampler;
uniform sampler2D NoiseSampler;
uniform float Time;
varying vec2 texCoord;
varying vec2 oneTexel;
varying vec4 outPos;
uniform vec2 InSize;
uniform float NoiseAmplification;
uniform float IntensityAdjust;
uniform float XOffset;
uniform float RedValue;
uniform float GreenValue;
uniform float BlueValue;


uniform float GPNVGMode;
uniform float PVS14Mode;
uniform float PVS7Mode;

const float SOFTNESS = 0.25;
const float contrast = 0.8;
const vec3 SEPIA = vec3(1.2, 1.0, 0.8);

void main() {
    vec4 texColor = texture2D(DiffuseSampler, texCoord.xy);
    texColor.rgb *= Brightness;

    if (NightVisionEnabled > 0.0) {
        vec2 uv;
        uv.x = 0.35 * sin(Time * 10.0);
        uv.y = 0.35 * cos(Time * 10.0);
        vec3 noise = texture2D(NoiseSampler, texCoord.xy + uv).rgb * NoiseAmplification;
        texColor.xy += noise.xy * 0.005;
    }

    if (VignetteEnabled > 0.0) {
        float dist = distance(texCoord.xy, vec2(0.5, 0.5));
        float vignette = smoothstep(VignetteRadius, VignetteRadius - SOFTNESS, dist);
        texColor.rgb *= vignette;
    }

    float alpha = 1.0;

    if (NightVisionEnabled > 0.0) {
        vec2 scaledCoord;
        bool inside = false;

        if (GPNVGMode > 0.0) {
            vec2 center = vec2(0.5, 0.5);
            scaledCoord = (texCoord.xy - center) * vec2(InSize.x / InSize.y, 1.0);
            float distCenter = length(scaledCoord);
            inside = inside || (distCenter <= 0.4);

            vec2 left = vec2(0.25, 0.5);
            scaledCoord = (texCoord.xy - left) * vec2(InSize.x / InSize.y, 1.0);
            float distLeft = length(scaledCoord);
            inside = inside || (distLeft <= 0.4);

            vec2 right = vec2(0.75, 0.5);
            scaledCoord = (texCoord.xy - right) * vec2(InSize.x / InSize.y, 1.0);
            float distRight = length(scaledCoord);
            inside = inside || (distRight <= 0.4);

            if (inside) {
                const vec3 lumvec = vec3(0.30, 0.59, 0.11);
                float intensity = dot(lumvec, texColor.rgb);
                intensity = clamp(contrast * (intensity - 0.5) + 0.5, 0.0, 1.0);
                float color = clamp(intensity / 0.59, 0.0, 1.0) * IntensityAdjust;
                vec4 visionColor = vec4(RedValue * color, GreenValue * color, BlueValue * color, 1.0);
                float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
                vec4 grayColor = vec4(gray, gray, gray, 1.0);
                texColor = grayColor * visionColor;
            }
        } else if (PVS14Mode > 0.0) {
            vec2 center = vec2(0.5 + XOffset, 0.5);
            vec2 scaledCoord = (texCoord.xy - center) * vec2(InSize.x / InSize.y, 1.0);
            float dist = length(scaledCoord);

            if (dist <= 0.4) {
                const vec3 lumvec = vec3(0.30, 0.59, 0.11);
                float intensity = dot(lumvec, texColor.rgb);
                intensity = clamp(contrast * (intensity - 0.5) + 0.5, 0.0, 1.0);
                float color = clamp(intensity / 0.59, 0.0, 1.0) * IntensityAdjust;
                vec4 visionColor = vec4(RedValue * color, GreenValue * color, BlueValue * color, 1.0);
                float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
                vec4 grayColor = vec4(gray, gray, gray, 1.0);
                texColor = grayColor * visionColor;
            }
        } else if (PVS7Mode > 0.0) {
            vec2 center = vec2(0.5, 0.5);
            vec2 scaledCoord = (texCoord.xy - center) * vec2(InSize.x / InSize.y, 1.0);
            float dist = length(scaledCoord);

            if (dist <= 0.4) {
                const vec3 lumvec = vec3(0.30, 0.59, 0.11);
                float intensity = dot(lumvec, texColor.rgb);
                intensity = clamp(contrast * (intensity - 0.5) + 0.5, 0.0, 1.0);
                float color = clamp(intensity / 0.59, 0.0, 1.0) * IntensityAdjust;
                vec4 visionColor = vec4(RedValue * color, GreenValue * color, BlueValue * color, 1.0);
                float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
                vec4 grayColor = vec4(gray, gray, gray, 1.0);
                texColor = grayColor * visionColor;
            }
        } else {
            const vec3 lumvec = vec3(0.30, 0.59, 0.11);
            float intensity = dot(lumvec, texColor.rgb);
            intensity = clamp(contrast * (intensity - 0.5) + 0.5, 0.0, 1.0);
            float color = clamp(intensity / 0.59, 0.0, 1.0) * IntensityAdjust;
            vec4 visionColor = vec4(RedValue * color, GreenValue * color, BlueValue * color, 1.0);
            float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
            vec4 grayColor = vec4(gray, gray, gray, 1.0);
            texColor = grayColor * visionColor;
        }
    }

    if (SepiaRatio > 0.0) {
        float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
        vec4 sepiaColor = vec4(vec3(gray) * SEPIA, 1.0);
        texColor = mix(texColor, sepiaColor, SepiaRatio);
    }

    gl_FragColor = vec4(texColor.rgb, 1);
    texColor = texture2D(DiffuseSampler, texCoord.xy);
    float brightness = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
    if (brightness < 0.8) {
        gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0); // Set to black if too dark
    } else {
        gl_FragColor = texColor;
    }
}
