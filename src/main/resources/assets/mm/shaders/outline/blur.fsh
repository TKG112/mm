#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 BlurDir;
uniform float Radius;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main() {
    vec4 blurred = vec4(0.0);
    float totalStrength = 0.0;
    float totalAlpha = 0.0;
    float totalSamples = 0.0;

    // Vanilla uses a simple box blur with radius
    for(float r = -Radius; r <= Radius; r += 1.0) {
        vec4 sample = texture(DiffuseSampler, texCoord + oneTexel * r * BlurDir);

    // Accumulate color weighted by alpha
    blurred.rgb += sample.rgb * sample.a;
    totalAlpha += sample.a;
    totalSamples += 1.0;
    }

    // Normalize
    if (totalAlpha > 0.0) {
        blurred.rgb /= totalAlpha;
        blurred.a = totalAlpha / totalSamples;
    }

    fragColor = blurred;
}