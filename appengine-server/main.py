"""`main` is the top level module for your Flask application."""

# Import the Flask Framework
import json
from flask import Flask
from flask import jsonify
from flask.globals import request
from flask.wrappers import Response
from google.appengine.api import blobstore
from encoders import NDBModelEncoder
from models import GistModel
import cloudstorage as gcs

app = Flask(__name__)
# Note: We don't need to call run() since our application is embedded within
# the App Engine WSGI application server.

BUCKET = '/uyogist-1067.appspot.com'


@app.route('/')
def hello():
    """Return a friendly HTTP greeting."""
    return 'Hello World!'


@app.route('/api/gist', methods=['GET', 'POST'])
def api_gists():
    """Gists Endpoint"""
    if request.method == 'GET':
        gists = GistModel.query().fetch(20)
        resp = Response(json.dumps(gists, cls=NDBModelEncoder), mimetype='application/json', status=200)
        return resp
    elif request.method == 'POST':
        # data = request.get_json(force=True)
        gist = GistModel()
        gist.added_by = request.form['nick']
        gist.gist = request.form['gist']
        if request.files['img'] is not None:
            gcs_filename = BUCKET + '/blobstore_demo'
            blob_key = CreateFile(gcs_filename)
            gist.image = blob_key
        key = gist.put()

        resp = jsonify(dict(gist.to_dict(), **{'id': key.urlsafe()}))
        resp.status_code = 201
        return resp
    else:
        raise RuntimeError("Unimplemented HTTP method")


@app.route('/api/gist/<gist_id>', methods=['GET'])
def api_gist(gist_id):
    gists = {'1': 'gist 1', '2': 'gist 2', '3': 'gist 3'}

    if gist_id in gists:
        return jsonify({gist_id:gists[gist_id]})
    else:
        return not_found()


@app.errorhandler(404)
def not_found(error=None):
    """Return a custom 400 error."""
    message = {
        'status': 404,
        'message': 'Not Found: ' + request.url
    }
    resp = jsonify(message)
    resp.status_code = 404

    return resp

@app.errorhandler(400)
def not_found(error=None):
    """Return a custom 400 error."""
    message = {
        'status': 400,
        'message': 'Bad Request: ' + format(error)
    }
    resp = jsonify(message)
    resp.status_code = 400

    return resp

@app.errorhandler(500)
def application_error(e):
    """Return a custom 500 error."""
    message = {
        'status': 500,
        'message': 'Sorry, unexpected error: ' + format(e)
    }
    resp = jsonify(message)
    resp.status_code = 500

    return resp


def CreateFile(filename):
    """Create a GCS file with GCS client lib.

    Args:
      filename: GCS filename.

    Returns:
      The corresponding string blobkey for this GCS file.
    """
    # Create a GCS file with GCS client.
    with gcs.open(filename, 'w') as f:
        f.write('abcde\n')

    # Blobstore API requires extra /gs to distinguish against blobstore files.
    blobstore_filename = '/gs' + filename
    # This blob_key works with blobstore APIs that do not expect a
    # corresponding BlobInfo in datastore.
    return blobstore.create_gs_key(blobstore_filename)
