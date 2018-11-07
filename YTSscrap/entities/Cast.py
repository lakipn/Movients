import json


class Cast:
    def __init__(self, name, character_name, imdb, image, movie_id, movie_name) -> None:
        self.name = name
        self.character_name = character_name
        self.imdb = imdb
        self.image = image
        self.movie_id = movie_id
        self.movie_name = movie_name

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)
