#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D PrevSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;

uniform float Resolution;
uniform float MosaicSize;

uniform vec3 Gray;
uniform float Saturation;

uniform vec3 Phosphor;

out vec4 fragColor;

void main() {
    vec2 mosaicInSize = InSize / MosaicSize;
    vec2 fractPix = fract(texCoord * mosaicInSize) / mosaicInSize;
    vec2 pixelatedCoord = texCoord - fractPix;
    vec4 BaseTexel = texture(DiffuseSampler, pixelatedCoord);

    BaseTexel.rgb = BaseTexel.rgb - fract(BaseTexel.rgb * Resolution) / Resolution;

    float Luma = dot(BaseTexel.rgb, Gray);
    vec3 Chroma = BaseTexel.rgb - Luma;
    BaseTexel.rgb = (Chroma * Saturation) + Luma;

    vec4 PrevTexel = texture(PrevSampler, texCoord);

    BaseTexel.rgb = max(PrevTexel.rgb * Phosphor, BaseTexel.rgb);

    BaseTexel.a = 1.0;
    fragColor = BaseTexel;
}