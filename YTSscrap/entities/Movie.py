import json


class Movie:
    def __init__(self, imdb_code, title, slug, year, rating, runtime, description, youtube, language) -> None:
        self.imdb_code = imdb_code
        self.title = title
        self.slug = slug
        self.year = year
        self.rating = rating
        self.runtime = runtime
        self.description = description
        self.youtube = youtube
        self.language = language
        self.genres = []

    def set_genres(self, genres):
        self.genres = genres

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)
