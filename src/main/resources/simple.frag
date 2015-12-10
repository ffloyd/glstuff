#version 330
out vec4 out_color;

uniform Data {
    uniform vec4 color;
};

void main() {
    out_color = color;
}