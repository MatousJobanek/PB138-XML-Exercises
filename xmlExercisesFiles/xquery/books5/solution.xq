<prices>
{
let $doc := doc("test.xml")
return <price>{data($doc//book/price)*0.9}</price>
}
</prices>
