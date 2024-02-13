--would be good to put purchase_time in a timezone, but since caller does not pass it just saving without it
CREATE TABLE IF NOT EXISTS receipt
(
    receipt_id VARCHAR NOT NULL,
    retailer VARCHAR NOT NULL,
    purchase_date DATE NOT NULL,
    purchase_time TIMESTAMP NOT NULL,
    total NUMERIC(2) NOT NULL,
    points BIGINT NOT NULL,
    PRIMARY KEY (receipt_id)
);

--index receipt_id because that is what we are querying by
CREATE INDEX receipt_id ON receipt(receipt_id);

commit;

CREATE TABLE IF NOT EXISTS item
(
    item_id UUID NOT NULL,
    short_description VARCHAR NOT NULL,
    price NUMERIC(2),
    receipt_id VARCHAR NOT NULL,
    foreign key (receipt_id) references receipt(receipt_id),
    PRIMARY KEY (item_id)
);

CREATE INDEX item_id ON item(item_id);
CREATE INDEX item_receipt_id_FK ON item(receipt_id);

commit;