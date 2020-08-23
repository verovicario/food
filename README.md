# food
El objetivo de este proyecto es crear un dataset a partir  de la plataforma FatSecret. El mismo está referido a información nutricional de alimentos de la región USA. Este fue creado para analizar la información con próposito educativo. El código tiene mucho para ser mejorado, pero no contaba con el tiempo suficiente igual lo compartó por si puede serle útil a alguien. También se adjunta el dataset obtenido a partir del mismo.

Se utilizaron tres APIs de la platafarma FatSecret para crear un dataset con información nutricional, filtrando por ciertas categorias de alimentos y solamente recuperando información referida a 100 g como unidad de medida. Esto puede ser facilmente cambiado para obtener otro tipo de información.

Las APIs fueron:
1) https://platform.fatsecret.com/api/Default.aspx?screen=rapiauth2: Solicitud  de acceso para obtener access token.

Para hacer uso de las APIs se deben registrar como desarrollador para obtener client id y client secret necesarios en el proceso de autenticación
Se necesita un access token. Para obtener el mismo se puede hacer de la siguiente forma:

curl -u clientId:clientSecret  -d "grant_type=client_credentials&scope=basic" -X POST https://oauth.fatsecret.com/connect/token

Con el token obtenido se puede interactuar con las siguientes APIs. Reemplazar en el código con el token correcto.

2) foods.search: Realiza una búsqueda en la base de datos de alimentos utilizando la expresión de búsqueda especificada.
https://platform.fatsecret.com/api/Default.aspx?screen=rapiref2&method=foods.search
Los elementos alimenticios devueltos son los que mejor coinciden con la expresión de búsqueda especificada, ordenados por su relevancia para la expresión de búsqueda.
Con esta API recuperamos los ids de las comidas para poder usar la siguiente API y obtener los detalles nutricionales. 
El código relacionado a esto se puede encontrar en la clase Food.java, el método getGeneralInfo itera por cada categoria de alimentos por los cuales se quiere recuperar información. se filtra solamente por alimentos genericos y se arma una lista de ids.
Algo a tener en cuenta es que HAY QUE IMPLEMENTER HILOS,  e intentar mas de una vez las llamadas porque suele ser bloqueada la IP y devolver elementos nulos

3) food.get.v2: Devuelve información nutricional detallada para el alimento especificado. Se utilizaron los ids obtenidos previamente para hacer uso de esta API
https://platform.fatsecret.com/api/Default.aspx?screen=rapiref2&method=food.get.v2

El elemento alimenticio devuelto contiene información general sobre el alimento con información nutricional detallada para cada tamaño de porción estándar disponible. En nuestro caso filtramos solamente por 100 g.
El código relacionado esta en la clase Food.java, método getFoodDetails, éste metodo también es el encargado de crear el archivo CSV con la información recibida.

Cabe mencionar que un alimento puede estar en más de una categoria, ésto llevó a obtener información duplicada en nuestro dataset. Para eliminar estos registros se creo un método que genera el dataset final sin registros duplicados.
