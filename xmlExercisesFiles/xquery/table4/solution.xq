<table>
{
let $doc := doc("test.xml")
for $tr at $i in $doc//tr
return <tr id="row{$i}">{
 for $cell in $tr/td
 return $cell
}</tr>
}
</table>
