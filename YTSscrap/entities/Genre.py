import json


class Genre:
    def __init__(self, id, name) -> None:
        self.id = id
        self.name = name

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)
