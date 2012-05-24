<prices>
{
for $book in doc("test.xml")//book 
    where $book/price < 50 
return $book/price
}
</prices>
