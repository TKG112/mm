#version 150

// DiffuseSampler = blurred colored outline (blurFramebuffer1)
// EntityMask     = filled entity mask (maskFramebuffer)
uniform sampler2D DiffuseSampler;
uniform sampler2D EntityMask;
uniform float Radius;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main() {
    // amostra central (alpha do blur na posição)
    vec4 center = texture(DiffuseSampler, texCoord);

    // calcule o máximo alpha na vizinhança (expansão)
    float maxAlpha = 0.0;
    // Limitar Radius razoavelmente no lado CPU/Java é bom; aqui assumimos Radius pequeno (1..3)
    for (float x = -Radius; x <= Radius; x += 1.0) {
        for (float y = -Radius; y <= Radius; y += 1.0) {
            vec2 offset = vec2(x, y) * oneTexel;
            float a = texture(DiffuseSampler, texCoord + offset).a;
            maxAlpha = max(maxAlpha, a);
        }
    }

    // alpha expandido (fora do centro)
    float expanded = max(0.0, maxAlpha - center.a);

    // pegar máscara preenchida da entidade (1.0 dentro, 0.0 fora)
    float maskA = texture(EntityMask, texCoord).a;

    // zere qualquer expansão que caia dentro da entidade (evita preto "invadindo" o corpo)
    float outerOnly = expanded * (1.0 - maskA);

    // suavização/falha controlável para evitar bordas duras:
    // ajuste os parâmetros do smoothstep para encurtar/alongar a transição
    float alpha = smoothstep(0.02, 0.7, outerOnly);

    // saída: preto com alpha
    fragColor = vec4(0.0, 0.0, 0.0, alpha);
}
