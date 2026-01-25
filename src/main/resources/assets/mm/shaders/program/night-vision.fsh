#version 150

uniform float NightVisionEnabled;
uniform float VignetteEnabled;
uniform float AutoGainEnabled;
uniform float AutoGatingEnabled;
uniform float VignetteRadius;
uniform float Brightness;
uniform float SepiaRatio;
uniform float UseMask;
uniform sampler2D DiffuseSampler;
uniform sampler2D NoiseSampler;
uniform sampler2D AutoGainSampler;
uniform sampler2D AutoGatingSampler;
uniform sampler2D MaskSampler;
uniform float Time;

in vec2 texCoord;
in vec2 oneTexel;
in vec4 outPos;

uniform vec2 InSize;
uniform float NoiseAmplification;
uniform float IntensityAdjust;
uniform float RedValue;
uniform float GreenValue;
uniform float BlueValue;
uniform float NoiseMultiplier;

out vec4 fragColor;

const float SOFTNESS = 0.25;
const float contrast = 0.8;
const vec3 SEPIA = vec3(1.2, 1.0, 0.8);

void main() {
    vec4 sceneColor = texture(DiffuseSampler, texCoord.xy);

    vec4 nvgColor = sceneColor;

    float finalGain;

    if (AutoGainEnabled > 0.5) {
        float autoGainValue = texture(AutoGainSampler, vec2(0.25, 0.25)).r;
        finalGain = autoGainValue;
    } else if (AutoGatingEnabled > 0.5) {
        float gatingOffset = texture(AutoGatingSampler, vec2(0.25, 0.25)).r;
        finalGain = Brightness + gatingOffset;
    } else {
        finalGain = Brightness;
    }

    nvgColor.rgb *= finalGain;

    if (NightVisionEnabled > 0.0) {
        vec2 uv;
        uv.x = 0.35 * sin(Time * 10.0);
        uv.y = 0.35 * cos(Time * 10.0);
        vec3 noise = texture(NoiseSampler, texCoord.xy + uv).rgb * NoiseAmplification;
        float gainBasedNoise = finalGain * NoiseMultiplier;

        nvgColor.xy += noise.xy * 0.005 * gainBasedNoise;
    }

    if (VignetteEnabled > 0.0) {
        float dist = distance(texCoord.xy, vec2(0.5, 0.5));
        float vignette = smoothstep(VignetteRadius, VignetteRadius - SOFTNESS, dist);
        nvgColor.rgb *= vignette;
    }

    if (NightVisionEnabled > 0.0) {
        const vec3 lumvec = vec3(0.30, 0.59, 0.11);

        float intensity = dot(lumvec, nvgColor.rgb);
        intensity = clamp(contrast * (intensity - 0.5) + 0.5, 0.0, 1.0);
        float color = clamp(intensity / 0.59, 0.0, 1.0) * IntensityAdjust;

        vec4 visionColor = vec4(RedValue * color, GreenValue * color, BlueValue * color, 1.0);

        float gray = dot(nvgColor.rgb, vec3(0.299, 0.587, 0.114));
        vec4 grayColor = vec4(gray, gray, gray, 1.0);

        nvgColor = grayColor * visionColor;
    }

    if (SepiaRatio > 0.0) {
        float gray = dot(nvgColor.rgb, vec3(0.299, 0.587, 0.114));
        vec4 sepiaColor = vec4(vec3(gray) * SEPIA, 1.0);
        nvgColor = mix(nvgColor, sepiaColor, SepiaRatio);
    }

    if (NightVisionEnabled > 0.0) {
        if (UseMask > 0.5) {
            float maskValue = texture(MaskSampler, texCoord).r;

            fragColor = mix(sceneColor, nvgColor, maskValue);
        } else {
            fragColor = nvgColor;
        }
    } else {
        fragColor = sceneColor;
    }

    fragColor.a = 1.0;
}