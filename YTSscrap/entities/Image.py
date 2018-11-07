import json


class Image:
    def __init__(self, medium, large, type, movie_id, movie_name) -> None:
        self.medium = medium
        self.large = large
        self.type = type
        self.movie_id = movie_id
        self.movie_name = movie_name

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)
