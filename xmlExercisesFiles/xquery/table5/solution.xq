<table>
<tr>
{
let $doc := doc("test.xml")
for $tr at $i in $doc/table/tr[1]/td
return <th>column {$i}</th>
}
</tr>
{
let $doc := doc("test.xml")
return $doc/table/tr
}
</table>
