echo "Henter siste test-jars for sakogbehandling-klient og okonomistotte-klient"

curl http://maven.adeo.no/nexus/service/local/repositories/m2internal/content/no/nav/vedtak/felles/integrasjon/sakogbehandling-klient/maven-metadata.xml -o maven-metadata-sb.xml -s
sbversion=$(grep "<version>" maven-metadata-sb.xml | sed 's/<[^>]*>//g'| awk '{ print $1 }' | tail -n 1)
echo "Siste versjon sak og behandling er $sbversion"
rm maven-metadata-sb.xml

curl http://maven.adeo.no/nexus/service/local/repositories/m2internal/content/no/nav/vedtak/felles/integrasjon/okonomistotte-jms/maven-metadata.xml -o maven-metadata-o.xml -s
oversion=$(grep "<version>" maven-metadata-o.xml | sed 's/<[^>]*>//g'| awk '{ print $1 }' | tail -n 1)
echo "Siste versjon okonomistotte er $oversion"
rm maven-metadata-o.xml

curl http://maven.adeo.no/nexus/service/local/repositories/m2internal/content/no/nav/vedtak/felles/integrasjon/felles-integrasjon-jms/maven-metadata.xml -o maven-metadata-jms.xml -s
jmsversion=$(grep "<version>" maven-metadata-jms.xml | sed 's/<[^>]*>//g'| awk '{ print $1 }' | tail -n 1)
echo "Siste versjon felles-integrasjon-jms er $jmsversion"
rm maven-metadata-jms.xml

echo "Henter sak og behandling..."
wget "http://maven.adeo.no/nexus/service/local/repositories/m2internal/content/no/nav/vedtak/felles/integrasjon/sakogbehandling-klient/$sbversion/sakogbehandling-klient-$sbversion-tests.jar" -O sakogbehandling-k$

echo "Henter okonomi..."
wget "http://maven.adeo.no/nexus/service/local/repositories/m2internal/content/no/nav/vedtak/felles/integrasjon/okonomistotte-jms/$oversion/okonomistotte-jms-$oversion-tests.jar" -O okonomi.jar

echo "Henter jms"
wget "http://maven.adeo.no/nexus/service/local/repositories/m2internal/content/no/nav/vedtak/felles/integrasjon/felles-integrasjon-jms/$jmsversion/felles-integrasjon-jms-$jmsversion-tests.jar" -O felles-integrasjon-jms.jar
