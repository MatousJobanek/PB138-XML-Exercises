<table>
{
let $doc := doc("test.xml")
for $row at $i in $doc/table/tr 
return <tr class= "{if ($i mod 2 = 0) then ("even") else ("odd")}">{ $row/td }</tr>
}
</table>
