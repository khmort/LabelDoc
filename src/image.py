import numpy as np
from PIL import Image
import sys


def col_has_dark_pixel(pixels: np.ndarray, col):
    return np.any(pixels[:, int(col)] < 0.5)


def row_has_dark_pixel(pixels: np.ndarray, row):
    return np.any(pixels[int(row), :] < 0.5)


def get_occupied_box(pixels: np.ndarray):
    col = 0
    while col < pixels.shape[1] and not col_has_dark_pixel(pixels, col):
        col += 1
    left = col

    col = pixels.shape[1] - 1
    while col > -1 and not col_has_dark_pixel(pixels, col):
        col -= 1
    right = col

    row = 0
    while row < pixels.shape[0] and not row_has_dark_pixel(pixels, row):
        row += 1
    up = row

    row = pixels.shape[0] - 1
    while row > -1 and not row_has_dark_pixel(pixels, row):
        row -= 1
    down = row
    return [left, up, right, down]


def search_for_bbox(norm_rect, margin, img_path):
    # load the target image
    img = Image.open(img_path).convert('1')

    # calculate the real size of crop-rect
    crop_rect = [
        norm_rect[0] * img.width,
        norm_rect[1] * img.height,
        norm_rect[2] * img.width,
        norm_rect[3] * img.height
    ]
    # crop image by `crop_rect`
    cropped = img.crop(crop_rect)

    # convert to numpy array for speed up searching process
    pixels = np.array(cropped, dtype=np.float16)

    # remove trash variable
    del cropped

    # search for the smallest occupied box
    rect = get_occupied_box(pixels)

    # make normilize
    rect = [
        (rect[0] + crop_rect[0] - margin[0]) / img.width,
        (rect[1] + crop_rect[1] - margin[1]) / img.height,
        (rect[2] + crop_rect[0] + margin[0]) / img.width,
        (rect[3] + crop_rect[1] + margin[1]) / img.height,
    ]
    return rect


args = sys.argv

x1 = float(args[1])
y1 = float(args[2])
x2 = float(args[3])
y2 = float(args[4])
x_margin = float(args[5])
y_margin = float(args[6])
img_path = args[7]

occupied_box = search_for_bbox(
    norm_rect=[x1, y1, x2, y2],
    margin=[x_margin, y_margin],
    img_path=img_path)

print(occupied_box)