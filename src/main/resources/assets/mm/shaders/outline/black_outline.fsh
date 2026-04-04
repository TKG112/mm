#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D EntityMask;
uniform float Radius;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main() {
    vec4 center = texture(DiffuseSampler, texCoord);

    float maxAlpha = center.a;

    maxAlpha = max(maxAlpha, texture(DiffuseSampler, texCoord + vec2(Radius * oneTexel.x, 0.0)).a);
    maxAlpha = max(maxAlpha, texture(DiffuseSampler, texCoord - vec2(Radius * oneTexel.x, 0.0)).a);
    maxAlpha = max(maxAlpha, texture(DiffuseSampler, texCoord + vec2(0.0, Radius * oneTexel.y)).a);
    maxAlpha = max(maxAlpha, texture(DiffuseSampler, texCoord - vec2(0.0, Radius * oneTexel.y)).a);

    float diag = Radius * 0.707;
    maxAlpha = max(maxAlpha, texture(DiffuseSampler, texCoord + vec2(diag * oneTexel.x, diag * oneTexel.y)).a);
    maxAlpha = max(maxAlpha, texture(DiffuseSampler, texCoord + vec2(-diag * oneTexel.x, diag * oneTexel.y)).a);
    maxAlpha = max(maxAlpha, texture(DiffuseSampler, texCoord + vec2(diag * oneTexel.x, -diag * oneTexel.y)).a);
    maxAlpha = max(maxAlpha, texture(DiffuseSampler, texCoord + vec2(-diag * oneTexel.x, -diag * oneTexel.y)).a);

    float expanded = max(0.0, maxAlpha - center.a);

    float maskA = texture(EntityMask, texCoord).a;

    float outerOnly = expanded * (1.0 - maskA);

    float alpha = smoothstep(0.02, 0.7, outerOnly);

    fragColor = vec4(0.0, 0.0, 0.0, alpha);
}