#version 460 core

layout(location = 1) in vec4 color;
layout(location = 2) in vec2 textureCoords;
layout(location = 3) flat in float texSlot;

layout(location = 0) out vec4 outColor;

void main()
{
    outColor = color;
}

