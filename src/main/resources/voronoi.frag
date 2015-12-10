#version 330
out vec4 fragColor;

uniform vec2 seeds[32];
uniform vec3 colors[32];
uniform int seeds_count;

float dist(vec2 a, vec2 b) {
    return distance(a, b);
}

int getNearestPoint() {
    int nearestIndex = 0;
    float nearestDist = dist(seeds[nearestIndex], gl_FragCoord.xy);

    for (int i = 1; i < seeds_count; ++i) {
        float current = distance(seeds[i], gl_FragCoord.xy);
        if (current < nearestDist) {
            nearestDist = current;
            nearestIndex = i;
        }
    }

    return nearestIndex;
}

vec3 shadeColor(vec2 center, vec3 base_color) {
    float h_1 = -0.005;
    float I_l = 1.0;
    float I_a = 0.4;
    vec3  l = normalize(vec3(0.1, 0.1, 1.0));

    vec3 norm = normalize(vec3(
        2.0 * h_1 * (center.x - gl_FragCoord.x),
        2.0 * h_1 * (center.y - gl_FragCoord.y),
        1.0
    ));

    float intensity = I_a + I_l * max(0, dot(l, norm) / (length(l) * length(norm)));
    return base_color * intensity;
}

vec3 getColor() {
    int nearestIndex = getNearestPoint();
    vec2 nearest = seeds[nearestIndex];

    float nearestDist = dist(nearest, gl_FragCoord.xy);

    if (nearestDist < 2.0) {
        return vec3(0.0, 0.0, 0.0); // draw points
    } else {
        return shadeColor(nearest, colors[nearestIndex]);
    }
}

void main() {
    vec3 color = getColor();

    fragColor = vec4(color, 1.0);
}