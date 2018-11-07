import json
import time

import cfscrape
import requests

from entities.Cast import Cast
from entities.Genre import Genre
from entities.Image import Image
from entities.Movie import Movie
from entities.Torrent import Torrent

host = 'https://yts.am/api/v2/'

# create arrays
genres = {}

scrapper = cfscrape.create_scraper(delay=11)

local_url = "http://localhost:8080/api/"
# headers = {'Accept': 'application/json', 'Content-Type': 'application/json',
# 'X-XSRF-TOKEN': 'aec1743f-e945-425a-a72a-325c2dcbd396'}
headers = {'Accept': 'application/json', 'Content-Type': 'application/json',
           'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTU0MTYyNzIxMH0.LTTkQjB2B3OXU6U0TdhCb9AJ1GFP1CYmFvn9qrwF195yAfjLoW0xxgL3F3jFXYJZCDauAaBC9ARRcLH8556tsw'}
# cookies = {'XSRF-TOKEN': 'aec1743f-e945-425a-a72a-325c2dcbd396',
#            'access_token': 'eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbIm9wZW5pZCJdLCJleHAiOjE1Mzk5MzU4MjAsImlhdCI6MTUzOTkzNTUyMCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiI2OWVhMmI5Ni0zMGU3LTQ4ZTMtYWJlZS0wNTk2MWQ3ODlhNDgiLCJjbGllbnRfaWQiOiJ3ZWJfYXBwIn0.fGYBDGM1im8WKnS6j-xSwmgGPtdUUTX2sXBpl_UvOTwAA2pTkdvmyClQIhVO0DKa0Faxj-KlH5AO5X4nBKfHVSjhK9U94w_qIqgJcwTqv9YQ5PS9sIGdBnPTcuF4uMPd2s72cIcFKVNKz3qioIwJtBg6WfDcN6YN9jtYMR7IpDAM5u_uCYTeNMKqGvlMZhJ9aQ7DMThgKOWdrbQ5tKv_VmfgsA_619YZ4gfAaAFutU_MHm2kv7CAoMJLRVghmRaC7YZuG1N9RTnnAmdHfgXi5nmMH_c2OoYG4n6BD7IlcbEtAThdzOO6L_6sxtApQLCzEIW671mTo9zG2SqlHZ3PtA',
#            'io': 'CdOETyeX6fgGIBMhAAAC',
#            'session_token': 'eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbIm9wZW5pZCJdLCJhdGkiOiI2OWVhMmI5Ni0zMGU3LTQ4ZTMtYWJlZS0wNTk2MWQ3ODlhNDgiLCJleHAiOjE1NDA1NDAzMjAsImlhdCI6MTUzOTkzNTUyMCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiI4YzRiNTYyYi1hODZkLTRhM2QtYjdkYy04Nzg4OTRmM2E1YTciLCJjbGllbnRfaWQiOiJ3ZWJfYXBwIn0.Lgd7wks_cgvuCuN5UxDhw27p3OQHkor6C8dnH2DOavE2a4l5yLmAgBex7tP1NthL4WM1QXz0HkssGZEX2Sby4PTBuxBzjl96eAhUopZxrFbNTlVN6-6kAd-ZxYZCZGg-4z_z1jhDNUYIYAaff-ZFmlOe6aWFV1-bKdMkKfH_YeAH8d5DvXmcY423FhexmM0TbB_cBUjAiBQPo-jMrk7diQ4m45la47a3N1EDhvfomZK6WhSuc1lpHzb_fgnijuRr4qGQdwMZMkMvcxl0cn3fcUXoTdKo8wjfTGcXsZGtgH0IBzqwelQG4p22E73cv0oX4pALrqaa_00sg4ehjfK5ZA'}

counter = 0
bad_req = 0
success_req = 0

bad = []


def underscore_to_camelcase(value):
    def camelcase():
        yield str.lower
        while True:
            yield str.capitalize

    c = camelcase()
    return "".join(next(c)(x) if x else '_' for x in value.split("_"))


def get_genres():
    global genres
    r = requests.get(local_url + 'genres?size=100', headers=headers)
    time.sleep(.03)
    tmp = json.loads(r.content.decode('utf-8'))
    for g in tmp:
        genres[g['name']] = Genre(g['id'], g['name'])


def rest_save(tmp_obj, route):
    global counter, success_req, bad_req
    obj = json.loads(tmp_obj.toJSON())

    for key in obj.keys():
        if key.find('_') > -1:
            obj[underscore_to_camelcase(key)] = obj.pop(key)

    r = requests.post(local_url + route, data=json.dumps(obj), headers=headers)
    time.sleep(.03)
    counter = counter + 1
    if 199 < r.status_code < 300:
        success_req = success_req + 1
        # print(json.dumps(obj))
    else:
        bad_req = bad_req + 1
        bad.append(obj.toJSON())
    print('{:d}::  {:d} | HTTP POST  {:s}'.format(counter, r.status_code, (local_url + route)))
    return r.json(), obj


# get ALL movies with all data
def get_movies(page):
    url = host + 'list_movies.json?limit=50&page={0}'.format(page)
    print(url)
    # r = requests.get(url)
    # time.sleep(10)
    r = scrapper.get(url)
    data = json.loads(r.content.decode('utf-8'))['data']
    # print(json.dumps(data))
    movies = data['movies']

    for movie_j in movies:
        # a movie
        movie = Movie(movie_j['imdb_code'], movie_j['title'], movie_j['slug'], movie_j['year'], movie_j['rating'],
                      movie_j['runtime'], movie_j['description_full'],
                      'https://www.youtube.com/watch?v=' + movie_j['yt_trailer_code'], movie_j['language'])

        # GET details
        details_response = json.loads(scrapper.get(
            host + 'movie_details.json?movie_id={0}&with_images=true&with_cast=true'.format(
                movie_j['id'])).content.decode('utf-8'))
        if 'data' in details_response and 'movie' in details_response['data']:
            details = ['data']['movie']
            # genres
            movie_genres = []
            if 'genres' in movie_j:
                for genre in movie_j['genres']:
                    if genre not in genres:
                        tmp_genre = Genre(None, genre)
                        rest_save(tmp_genre, 'genres')
                        time.sleep(1)  # cache related.
                        get_genres()
                        movie_genres.append(genres[genre])
                movie.set_genres(movie_genres)

            # saving a movie object and saving the movie_id for later use
            print('Saving a movie ~~~')
            ret, tmp_obj = rest_save(movie, 'movies')
            movie_id = ret['id']

            # cast
            cast = []
            if 'cast' in details:
                for cast_j in details['cast']:
                    cast.append(Cast(cast_j['name'], cast_j['character_name'], cast_j['imdb_code'],
                                     get_imdb_image(cast_j['imdb_code']), movie_id, movie_j['title']))
            print('Saving cast ~~~')
            for c in cast:
                rest_save(c, 'casts')

            # images
            images = [
                Image(movie_j['medium_cover_image'], movie_j['large_cover_image'], 'COVER', movie_id, movie_j['title'])]
            for i in [1, 2, 3, 4, 5, 6, 7, 8, 9]:
                if 'medium_screenshot_image{0}'.format(i) not in details or 'large_screenshot_image{0}'.format(
                        i) not in details:
                    break
                images.append(
                    Image(details['medium_screenshot_image{0}'.format(i)], details['large_screenshot_image{0}'.format(i)],
                          'SCREENSHOT', movie_id, movie_j['title']))
            print('Saving images ~~~')
            for i in images:
                rest_save(i, 'images')

            # torrents
            if 'torrents' in movie_j:
                torrents = []
                for torrent in movie_j['torrents']:
                    torrents.append(Torrent(torrent['url'], torrent['hash'], torrent['quality'], torrent['size'], movie_id,
                                            movie_j['title']))
                print('Saving torrents ~~~')
                for t in torrents:
                    rest_save(t, 'torrents')

            print('\n~~~~~~~~~~ MOVIE SAVED | ID={0} | From page={1} ~~~~~~~~~~\n'.format(movie_id, page))

    return data['movie_count']


def get_imdb_image(act_id):
    # r = requests.get('http://www.imdb.com/name/nm{0}'.format(act_id))
    # soup = BeautifulSoup(r.content.decode('utf-8'), features="lxml")
    # img_src = soup.find("img", {"id": "name-poster"})['src']
    # return img_src if len(img_src) > 0 else ''
    return 'https://img.yts.am/assets/images/actors/thumb/nm{0}.jpg'.format(act_id)


get_genres()
page = 0
movie_count = 1251
while page * 50 < movie_count:
    page = page + 1
    movie_count = get_movies(page)

for genre in genres:
    print(genre, sep=', ')
    # transform json data to appropriate arrays and dicts
    # for every movie get cast and images
    # POST everything related to a movie to server
