#version 330 core
out vec4 result;

uniform int points_count;
uniform vec2 points[64];

uniform int generators_count;
uniform vec3 colors[64];
uniform int endpoints[64];

float eps = 0.000001;

struct IndexRange {
    int left;
    int right;
};

float dist(vec2 a, vec2 b) {
    return distance(a, b);
}

IndexRange getGeneratorPointsRange(int generatorIndex) { // [0] - first element, [1] - last + 1
    if (generatorIndex == 0) {
        return IndexRange(0, endpoints[0]);
    } else {
        return IndexRange(endpoints[generatorIndex - 1], endpoints[generatorIndex]);
    }
}

vec2 getClosestSegmentPoint(vec2 segment_start, vec2 segment_end, vec2 point) {
    vec2 segment_vec    = segment_end - segment_start;
    vec2 point_vec      = point - segment_start;

    float projection_shift = dot(point_vec, segment_vec) / dot(segment_vec, segment_vec);
    if (projection_shift < -eps) {
        return segment_start;
    } else if (projection_shift > (1.0 + eps)) {
        return segment_end;
    } else {
        return segment_vec * projection_shift + segment_start;
    }
}

vec2 getClosestGeneratorPoint(int generatorIndex) {
    IndexRange range = getGeneratorPointsRange(generatorIndex);

    vec2 bestPoint = points[range.left];
    float bestDist = dist(gl_FragCoord.xy, bestPoint);

    for (int i = range.left + 1; i < range.right; ++i) {
        vec2 currentPoint = getClosestSegmentPoint(points[i-1], points[i], gl_FragCoord.xy);
        float currentDist = dist(gl_FragCoord.xy, currentPoint);

        if (currentDist < bestDist) {
            bestPoint = currentPoint;
            bestDist = currentDist;
        }
    }

    return bestPoint;
}

float distToGenerator(int generatorIndex) {
    return dist(getClosestGeneratorPoint(generatorIndex), gl_FragCoord.xy);
}

int getClosestGeneratorIndex() {
    int bestIndex = 0;
    float best = distToGenerator(bestIndex);

    for (int i = 1; i < generators_count; ++i) {
        float current = distToGenerator(i);
        if (current < best) {
            best = current;
            bestIndex = i;
        }
    }

    return bestIndex;
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
    int generatorIndex = getClosestGeneratorIndex();
    vec2 closestPoint = getClosestGeneratorPoint(generatorIndex);

    float distToGenerator = dist(closestPoint, gl_FragCoord.xy);

    if (distToGenerator < 1.0) {
        return vec3(0.0, 0.0, 0.0); // draw points
    } else {
        return shadeColor(closestPoint, colors[generatorIndex]);
    }
}

void main() {
    vec3 color = getColor();

    result = vec4(color, 1.0);
}
