import json
import datetime
from time import mktime
from google.appengine.api.datastore_types import BlobKey
from google.appengine.api.images import get_serving_url
from google.appengine.ext import ndb
from google.appengine.ext.ndb.key import Key


__author__ = 'oyewale'


class NDBModelEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, datetime.datetime):
            return int(mktime(obj.timetuple()))
        elif isinstance(obj, ndb.GeoPt):
            return {'lat': obj.lat, 'lon': obj.lon}
        elif isinstance(obj, ndb.Model):
            fence_dict = obj.to_dict()
            fence_dict['key'] = obj.key.urlsafe()
            return fence_dict
        elif isinstance(obj, Key):
            return obj.urlsafe()
        elif isinstance(obj, BlobKey):
            return get_serving_url(obj)

        return json.JSONEncoder.default(self, obj)