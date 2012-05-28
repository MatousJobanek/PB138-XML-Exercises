<sources>
{
let $doc := doc("test.xml")
for $source in $doc//book/source
return $source
}
</sources>
