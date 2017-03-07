import sys

from selenium import webdriver
import unittest

import util

class Test(unittest.TestCase):
    """ Demonstration: Get Chrome to generate fullscreen screenshot """

    def setUp(self):
        self.driver = webdriver.Chrome("/Users/raghunandan.gupta/Downloads/chromedriver")

    def tearDown(self):
        self.driver.quit()

    def test_fullpage_screenshot(self):
        ''' Generate document-height screenshot '''
        url = "https://techcrunch.com/"
        self.driver.get(url)
        util.fullpage_screenshot(self.driver, "test_1.png")


if __name__ == "__main__":
    unittest.main(argv=[sys.argv[0]])