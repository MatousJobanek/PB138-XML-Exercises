<sources>
{
let $doc := doc("test.xml")
for $source in $doc//source
return $source
}
</sources>
