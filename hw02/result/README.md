
| Threads | Without index                               | With index                               |
|---------|---------------------------------------------|------------------------------------------|
| 1       | ![image info](./img/1 without index.png)    | ![image info](./img/1 with index.png)    |
| 10      | ![image info](./img/10 without index.png)   | ![image info](./img/10 with index.png)   |
| 100     | ![image info](./img/100 without index.png)  | ![image info](./img/100 with index.png)  |
| 1000    | ![image info](./img/1000 without index.png) | ![image info](./img/1000 with index.png) |

```postgresql
CREATE INDEX users_firstname_secondname_index ON public.users (firstName, secondName);

EXPLAIN ANALYSE SELECT * FROM users WHERE firstName LIKE 'An%' and secondName LIKE 'B%';
```

![image info](./img/explain.png)

B-Tree позволяет осуществлять многоколоночный поиск