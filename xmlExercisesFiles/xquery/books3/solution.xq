<books>
{
let $doc := doc("test.xml")
for $book in $doc//book
order by $book/title
return $book
}
</books>

