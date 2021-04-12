create unlogged table products_import (doc json);

copy products_import from '/app-data/products.json';

insert into products (name, price, currency, category)
select p ->> 'name' as name,
    to_number(p ->> 'price', '999D99S') as price,
    p ->> 'currency' as currency,
    p ->> 'category' as category
from products_import
    cross join lateral json_array_elements(doc -> 'products') p;

drop table products_import;
