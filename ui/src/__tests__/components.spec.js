import { describe, it, expect } from 'vitest'

import { mount } from '@vue/test-utils'
import About from '../components/About.vue'
import Welcome from '../components/Welcome.vue'

describe('About', () => {
  it('renders properly', () => {
    const wrapper = mount(About, {})
    expect(wrapper.text()).toContain('SMS-U est un service numérique mutualisé')
  })
})
describe('Welcome', () => {
    it('renders properly', () => {
      const wrapper = mount(Welcome, {})
      expect(wrapper.text()).toContain('Bienvenue dans esup-smsu-api-admin')
    })
  })
  