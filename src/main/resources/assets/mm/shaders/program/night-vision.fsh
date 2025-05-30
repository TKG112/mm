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
uniform float RedValue;
uniform float GreenValue;
uniform float BlueValue;

const float SOFTNESS = 0.25;
const float contrast = 0.8;
const vec3 SEPIA = vec3(1.2, 1.0, 0.8);

void main() {
    vec4 texColor = texture2D(DiffuseSampler, texCoord.xy);
    texColor.rgb *= Brightness;
    
    if(NightVisionEnabled > 0) {
        vec2 uv;
        uv.x = 0.35 * sin(Time * 10);
        uv.y = 0.35 * cos(Time * 10);
        vec3 noise = texture2D(NoiseSampler, texCoord.xy + uv).rgb * NoiseAmplification;
        texColor.xy += noise.xy * 0.005;
    }

    if(VignetteEnabled > 0) {
        float dist = distance(texCoord.xy, vec2(0.5, 0.5));
        float vignette = smoothstep(VignetteRadius, VignetteRadius - SOFTNESS, dist);
        texColor.rgb *= vignette;
        texColor.a = 1.0;
    }

    if(NightVisionEnabled > 0) {
        vec2 center = vec2(0.5, 0.5);
        vec2 scaledCoord = (texCoord.xy - center) * vec2(InSize.x / InSize.y, 1.0);
        float dist = length(scaledCoord);

        if (dist <= 2) {  // Adjust this value to change the radius of the circle
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

    if(SepiaRatio > 0) {
        float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
        vec4 sepiaColor = vec4(vec3(gray) * SEPIA, 1.0);
        texColor = mix(texColor, sepiaColor, SepiaRatio);
    }

    gl_FragColor = vec4(texColor.rgb, 1);
}
