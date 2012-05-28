let $doc := doc("test.xml")
return <TotalQuantity>{sum($doc/Order/Item/Quantity)}</TotalQuantity>

