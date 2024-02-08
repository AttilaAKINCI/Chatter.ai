package com.akinci.chatter.core.network.json

object UserServiceResponse {
    val rawResponse = """
        {
            "results": [
                {
                    "gender": "male",
                    "name": {
                        "title": "Mr",
                        "first": "David",
                        "last": "Mota"
                    },
                    "location": {
                        "street": {
                            "number": 6902,
                            "name": "Prolongación Norte Arce"
                        },
                        "city": "Teoloyucan",
                        "state": "Chihuahua",
                        "country": "Mexico",
                        "postcode": 30046,
                        "coordinates": {
                            "latitude": "22.3574",
                            "longitude": "-39.4704"
                        },
                        "timezone": {
                            "offset": "-12:00",
                            "description": "Eniwetok, Kwajalein"
                        }
                    },
                    "email": "david.mota@example.com",
                    "login": {
                        "uuid": "390bed23-ab39-463a-a157-4434f69f3e12",
                        "username": "greenkoala365",
                        "password": "baylor",
                        "salt": "QToMznQi",
                        "md5": "8537f882fcae27d519aaa6fdfc87cfa9",
                        "sha1": "b0ca4010f263b6f4aa5c9d2518bcb0081376f13a",
                        "sha256": "f919bded18583d8ab28fe15447ca291d97f554f62dc8e918fd85e14d5d826786"
                    },
                    "dob": {
                        "date": "1961-11-29T09:21:48.552Z",
                        "age": 62
                    },
                    "registered": {
                        "date": "2006-02-25T14:17:16.923Z",
                        "age": 17
                    },
                    "phone": "(694) 591 9018",
                    "cell": "(606) 122 0010",
                    "id": {
                        "name": "NSS",
                        "value": "16 26 84 9967 5"
                    },
                    "picture": {
                        "large": "https://randomuser.me/api/portraits/men/29.jpg",
                        "medium": "https://randomuser.me/api/portraits/med/men/29.jpg",
                        "thumbnail": "https://randomuser.me/api/portraits/thumb/men/29.jpg"
                    },
                    "nat": "MX"
                }
            ],
            "info": {
                "seed": "3d45d3bd5bf8392b",
                "results": 1,
                "page": 1,
                "version": "1.4"
            }
        }
    """
}