drop table Product;
drop table orderline;

create table Product (
  id        int not null,
  name      varchar(200) not null,
  price     numeric(18,2) not null,
  group_id  int not null,
  primary key(id)
);

create table orderline(
  order_id int not null,
  line_id int not null,
  product_id int not null,
  primary key(order_id,line_id)
)





