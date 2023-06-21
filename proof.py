import math

def is_prime(n: int) -> bool:
    return all(n % p != 0 for p in range(2, 1 + math.isqrt(n)))

def primality_check(n):
    print('Primality Check')
    for u in range(n, n + 10):
        v = u + 1
        if is_prime(u*v - 1):
            print(u, v, u*v - 1)


def primitive_root_check(p):
    print('Primitive Root Check', p)
    for g in range(1, p):
        root = set(pow(g, n, p) for n in range(0, p - 1))
        if len(root) == p - 1:
            print(g)

def grid_check(u, v, g):
    print('Grid Check', u, v, g)
    p = u*v - 1
    for x in range(u):
        for y in range(v):
            q = x + u * y
            if q == p:
                print(x, y, '->', x, y)
            else:
                q1 = pow(g, q, p)
                x1 = q1 % u
                y1 = q1 // u
                print(x, y, '->', x1, y1)

# Examples:
# 3,4 -> 11 root 6
# 6,7 -> 41 root 19
# 96,97 -> 9311 root 143
# 203,204 -> 41411 root 229

