compila:
	javac src/cliente/Alarma.java src/cliente/Sensor.java
	cp src/cliente/*.class src/servidor/
	cd src/servidor; javac *.java; cd ../..
	cp src/servidor/ServicioAlarmas.class src/cliente/
	cd src/cliente; javac *.java; cd ../..

clean:
	rm src/cliente/*.class
	rm src/servidor/*.class