import json


class Torrent:
    def __init__(self, url, hash, quality, size, movie_id, movie_name) -> None:
        self.url = url
        self.hash = hash
        self.quality = quality
        self.size = size
        self.movie_id = movie_id
        self.movie_name = movie_name

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)
