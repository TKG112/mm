#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D PreviousGatingSampler;
uniform float AutoGatingEnabled;
uniform float AutoGatingSpeed;
uniform float AutoGatingOffset;
uniform float GatingThreshold;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

float luminance(vec3 rgb) {
    return max(max(rgb.r, rgb.g), rgb.b);
}

void main() {
    if (texCoord.x < 0.5 && texCoord.y < 0.5) {
        vec2 scaled = texCoord * 2.0;

        vec3 color = texture(DiffuseSampler, scaled).rgb
        + texture(DiffuseSampler, scaled + vec2(0.0, oneTexel.y)).rgb
        + texture(DiffuseSampler, scaled + vec2(oneTexel.x, 0.0)).rgb
        + texture(DiffuseSampler, scaled + oneTexel).rgb;

        float currentLuminance = luminance(color);

        float previousOffset = texture(PreviousGatingSampler, texCoord).r;

        if (AutoGatingEnabled > 0.5) {
            float targetOffset = 0.0;

            if (currentLuminance > GatingThreshold) {
                float brightness = (currentLuminance - GatingThreshold) / (1.0 - GatingThreshold);
                targetOffset = -AutoGatingOffset * brightness;
            } else if (currentLuminance < GatingThreshold * 0.7) {
                float darkness = 1.0 - (currentLuminance / (GatingThreshold * 0.7));
                targetOffset = AutoGatingOffset * darkness;
            }

            float newOffset = mix(previousOffset, targetOffset, AutoGatingSpeed);

            fragColor = vec4(vec3(newOffset), 1.0);
        } else {
            fragColor = vec4(vec3(mix(previousOffset, 0.0, 0.1)), 1.0);
        }
    } else {
        fragColor = texture(PreviousGatingSampler, texCoord);
    }
}