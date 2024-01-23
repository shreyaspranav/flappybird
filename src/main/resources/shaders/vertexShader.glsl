#version 460 core

layout(location = 0) in vec3 aPosition;
layout(location = 1) in vec4 aColor;
layout(location = 2) in vec2 aTextureCoords;
layout(location = 3) in float aTexSlot;

layout(location = 1) out vec4 color;
layout(location = 2) out vec2 textureCoords;
layout(location = 3) flat out float texSlot;

uniform mat4 uViewProjectionMatrix;

void main()
{
    gl_Position      = uViewProjectionMatrix * vec4(aPosition, 1.0);
    color            = aColor;
    textureCoords    = aTextureCoords;
    texSlot          = aTexSlot;
}

