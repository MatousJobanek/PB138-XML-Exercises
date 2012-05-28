<ul>
{
let $doc := doc("test.xml")
for $td in $doc//td
return <li>{data($td)}</li>
}
</ul>
