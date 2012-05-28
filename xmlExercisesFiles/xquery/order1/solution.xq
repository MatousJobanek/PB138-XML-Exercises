<TotalPrices>
{
let $doc := doc("test.xml")
for $item in $doc//Item
return <TotalPrice id="{$item/ID}">{$item/Quantity * $item/UnitPrice}</TotalPrice>
}
</TotalPrices>
